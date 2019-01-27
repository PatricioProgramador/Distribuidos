package Lamport;

import Juego.Jugador;
import Sockets.Servidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteLamport extends Thread{
    private static final String IP_ADDRESS="192.168.1.64";
    private static int PUERTO=3001;
    ObjectOutputStream out;
    ObjectInputStream in;
    private Socket conexion;
    private MensajeLamport mensaje;
    private Jugador cliente;
    public ClienteLamport(Jugador cliente)
    {
       this.cliente=cliente;
    }
    public void conectar() throws IOException
    {
        conexion=new Socket(IP_ADDRESS, PUERTO);
        out=new ObjectOutputStream(conexion.getOutputStream());
        in=new ObjectInputStream(conexion.getInputStream());
    }
    public void Enviar() throws IOException
    {
        mensaje=new MensajeLamport(cliente.getContador());
        System.out.println(mensaje.getContador());
        out.writeObject(mensaje);
    }
    public void setMensaje(MensajeLamport mensaje)
    {
        this.mensaje=mensaje;
    }
    public MensajeLamport getMensaje()
    {
        return mensaje;
    }

    public void run()
    {
        
        try {
            for(;;)
            {
                sleep(1000);
                Enviar();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClienteLamport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClienteLamport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void CerrarConexion() throws IOException
    {
        out.close();
        in.close();
        conexion.close();
    }
    /*public static void main(String[] args) throws IOException
    {
        System.out.println("Cliente");
        Jugador jug=new Jugador("123.122.55.6");
        ClienteLamport cl=new ClienteLamport(jug);
        cl.conectar();
        cl.start();
        //cl.Enviar();
        //cl.CerrarConexion();
    }*/
}
