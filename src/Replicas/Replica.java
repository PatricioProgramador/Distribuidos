package Replicas;

import Juego.Carta;
import static Sockets.Servidor.tope;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Replica extends Thread{
    private String ip_address;
    public Socket con;
    private final int PUERTO=3005;
    public ObjectOutputStream x;
    
    public Replica(String ip_address) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        this.ip_address=ip_address; 
        this.start();
    }
    
    public void run()
    {
        try {
            con=new Socket(this.ip_address, 3005);
            System.out.println(con.getRemoteSocketAddress().toString());
            this.x=new ObjectOutputStream(con.getOutputStream());
            CambiarTope(tope);
            //con.getInetAddress();
        } catch (IOException ex) {
            System.out.println("error en replica/run()" + ex);
        }
    }
    public void InsertarJugador(int idJugador, String host)
    {
        MensajeSQL mensaje = new MensajeSQL(1,idJugador,host);
        System.out.println(con.getRemoteSocketAddress().toString());

        try {
            //ObjectOutputStream x=new ObjectOutputStream(con.getOutputStream());
            x.writeObject(mensaje);
        } catch (IOException ex) {
            System.out.println("error en ProcesarReplicas/insertarjugador()"+ ex);
        }
    }

    public void InsertarCartaJugador(int idJugador, int idCarta)
    {
        MensajeSQL mensaje = new MensajeSQL(2,idJugador,idCarta);
        System.out.println(con.getRemoteSocketAddress().toString());

        try {
            //ObjectOutputStream x=new ObjectOutputStream(con.getOutputStream());
            x.writeObject(mensaje);
        } catch (IOException ex) {
            System.out.println("error en ProcesarReplicas/CartaJugador "+ ex);
        }
    }
    
    public void LimpiarCartaJugador()
    {
        MensajeSQL mensaje = new MensajeSQL(3);
        try {
            //ObjectOutputStream x=new ObjectOutputStream(con.getOutputStream());
            x.writeObject(mensaje);
        } catch (IOException ex) {
            System.out.println("error en ProcesarReplicas/enviarmensaje()");
        }
    }
    public void LimpiarJugador()
    {
        MensajeSQL mensaje = new MensajeSQL(4);
        try {
            //ObjectOutputStream x=new ObjectOutputStream(con.getOutputStream());
            x.writeObject(mensaje);
        } catch (IOException ex) {
            System.out.println("error en ProcesarReplicas/enviarmensaje()");
        }
    }
    public void EliminarCartaJugador(int idJugador, int idCarta)
    {
        MensajeSQL mensaje = new MensajeSQL(5,idJugador,idCarta);
        try {
            //ObjectOutputStream x=new ObjectOutputStream(con.getOutputStream());
            x.writeObject(mensaje);
        } catch (IOException ex) {
            System.out.println("error en ProcesarReplicas/enviarmensaje()");
        }
    }
    
    public void CambiarTope(Carta c) 
    {
        try{
        System.out.println("tope: "+ c.toString());
        System.out.println("flujo x "+x.toString());

        MensajeSQL mensaje= new MensajeSQL(6);
        mensaje.setCarta(c);
        x.writeObject(mensaje);
        }catch(IOException ex)
        {
            System.out.println("error en CambiarTope "+ ex);
        }
    }
    
    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public Socket getCon() {
        return con;
    }

    public void setCon(Socket con) {
        this.con = con;
    }
}
