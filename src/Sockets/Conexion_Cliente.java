package Sockets;

import ConexionBD.Conexion_BD;
import Juego.Carta;
import static Sockets.Servidor.clientes;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static practica5distr.GUIServidor.actualizarListas;
import static practica5distr.GUIServidor.jList1;
import static practica5distr.GUIServidor.jList2;
import static practica5distr.GUIServidor.jList3;
import static practica5distr.GUIServidor.jTextField1;
import static Sockets.Servidor.tope;
import static practica5distr.GUIServidor.jLabel6;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import static practica5distr.GUICliente.indice;
import static practica5distr.GUIServidor.SERVIDOR_REPLICAS;

public class Conexion_Cliente implements Runnable{
    private Socket con;
    private Mensaje mensaje;
    private ObjectInputStream flujo_entrada;
    private ObjectOutputStream flujo_salida;
    private Conexion_BD conBD;
    private List lista_cartas;
    private Image image;
    public Conexion_Cliente(Socket con, Mensaje mensaje, List lista_cartas) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        this.con=con;
        this.mensaje=mensaje;
        try{
            flujo_entrada= new ObjectInputStream(con.getInputStream());
            flujo_salida= new ObjectOutputStream(con.getOutputStream());
            conBD=new Conexion_BD("localhost");
            this.lista_cartas=lista_cartas;            
        }catch(IOException ex)
        {
            System.out.println("error en los stream ");
        }
    }
    public void run(){
        procesarhilo();
    }
    public Carta ObtenerAleatorio()
    {
        int indice=lista_cartas.size();
        System.out.println(indice);
        int aleatorio= (int)(Math.random()*indice);
        Carta x=(Carta)lista_cartas.get(aleatorio);
        lista_cartas.remove(aleatorio);
        return x;
    }

    public Socket getCon() {
        return con;
    }

    public void setCon(Socket con) {
        this.con = con;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public ObjectInputStream getFlujo_entrada() {
        return flujo_entrada;
    }

    public void setFlujo_entrada(ObjectInputStream flujo_entrada) {
        this.flujo_entrada = flujo_entrada;
    }

    public ObjectOutputStream getFlujo_salida() {
        return flujo_salida;
    }

    public void setFlujo_salida(ObjectOutputStream flujo_salida) {
        this.flujo_salida = flujo_salida;
    }

    public Conexion_BD getConBD() {
        return conBD;
    }

    public void setConBD(Conexion_BD conBD) {
        this.conBD = conBD;
    }

    public List getLista_cartas() {
        return lista_cartas;
    }

    public void setLista_cartas(List lista_cartas) {
        this.lista_cartas = lista_cartas;
    }
    //región crítica
    public synchronized void procesarhilo(){
        boolean conectado=true;
        
        while(conectado)
        {
            try {
                mensaje=(Mensaje)flujo_entrada.readObject();
                if(mensaje.getIdMensaje()==1)
                {
                    System.out.println(mensaje.getIp());
                    if(!conBD.BuscarJugador(mensaje.getIdJugador()))
                    {
                        conBD.InsertarJugador(mensaje.getIdJugador(), mensaje.getIp());
                        for(int i=0;i<SERVIDOR_REPLICAS.getReplicas().size();i++)
                            SERVIDOR_REPLICAS.getReplicas().get(i).InsertarJugador(mensaje.getIdJugador(), mensaje.getIp());
                    }
                    Carta c=ObtenerAleatorio();
                    conBD.InsertarCartaJugador(mensaje.getIdJugador(), c.getIdCarta());
                    for(int i=0;i<SERVIDOR_REPLICAS.getReplicas().size();i++)
                    {
                        SERVIDOR_REPLICAS.getReplicas().get(i).InsertarCartaJugador(mensaje.getIdJugador(), c.getIdCarta());
                        System.out.println("entra al for");

                    }
                        System.out.println("Listas");

                    actualizarListas();
                    flujo_salida.writeObject(c);
                }
                if(mensaje.getIdMensaje()==2)
                {
                    Carta res=mensaje.getCarta();
                    if((res.getValor()==tope.getValor()) || (res.getTipo().equalsIgnoreCase(tope.getTipo())))
                    {
                        tope=res;
                        conBD.EliminarCartaJugador(mensaje.getIdJugador(),mensaje.getCarta().getIdCarta());
                        for(int i=0;i<SERVIDOR_REPLICAS.getReplicas().size();i++)
                            SERVIDOR_REPLICAS.getReplicas().get(i).EliminarCartaJugador(mensaje.getIdJugador(),mensaje.getCarta().getIdCarta());

                        actualizarListas();
                        flujo_salida.writeObject(null);
                        jTextField1.setText(tope.getValor()+" "+tope.getTipo());
                        
                        //imagen
                        image=ImageIO.read(new File(tope.getUrl()));
                        ImageIcon imageIcon = new ImageIcon(image);
                        jLabel6.setIcon(imageIcon);
                    }
                    else
                    {
                        flujo_salida.writeObject(res);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Cliente Desconectado");
                conectado=false;
                try {
                    flujo_entrada.close();
                    flujo_salida.close();
                    clientes.remove(0);
                } catch (IOException ex1) {
                    System.out.println("error al cerrar los in/out");
                }
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Conexion_Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
    }
    public static void main(String[] args) throws SQLException
    {
        try {
            Conexion_BD conBD=new Conexion_BD("192.168.1.67");
            conBD.InsertarJugador(3, "registro");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Conexion_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
