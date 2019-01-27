package Sockets;

import Juego.Carta;
import Replicas.MensajeSQL;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import static Sockets.Cliente.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    public String IP_ADDRESS="localhost";
    public int PUERTO=3000;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    
    public ObjectOutputStream out_replicas;
    public ObjectInputStream in_replicas;
    public Socket conexion;
    public Socket conexionReplicas;

    //implementacion de conexion al servidor de replicas
    public List lista_ip;
    public Cliente() throws IOException, ClassNotFoundException
    {
        conexion= new Socket(IP_ADDRESS, PUERTO);
        //Conexion a replica
        conexionReplicas=new Socket(IP_ADDRESS, 3005);
        //asignar al objectoutpustream el flujo de salida del socket de conexion
        //System.out.println(conexionReplicas.getLocalAddress());

        out=new ObjectOutputStream(conexion.getOutputStream());
        in=new ObjectInputStream(conexion.getInputStream());
        //asignar flujos a la conexion del servidor
        out_replicas=new ObjectOutputStream(conexionReplicas.getOutputStream());
        in_replicas=new ObjectInputStream(conexionReplicas.getInputStream());
        PedirListaCartas();
    }
    public Cliente(String ip) throws IOException, ClassNotFoundException
    {
        conexion= new Socket(ip, PUERTO);
        //Conexion a replica
        conexionReplicas=new Socket(ip, 3005);
        //System.out.println(conexionReplicas.getInetAddress());
        //asignar al objectoutpustream el flujo de salida del socket de conexion
        out=new ObjectOutputStream(conexion.getOutputStream());
        in=new ObjectInputStream(conexion.getInputStream());
        //asignar flujos a la conexion del servidor
        out_replicas=new ObjectOutputStream(conexionReplicas.getOutputStream());
        in_replicas=new ObjectInputStream(conexionReplicas.getInputStream());
        PedirListaCartas();
    }
    public void PedirListaCartas() throws IOException, ClassNotFoundException{
        //System.out.println("Manda Msj ");
        HiloObtenerReplicas hilo=new HiloObtenerReplicas(this);
        hilo.start();
    }
    
    public Carta enviar(Mensaje peticion) throws IOException, ClassNotFoundException
    {
        //mandar objeto
        peticion.setIdMensaje(1);
        out.writeObject(peticion);
        //ObjectInputStream in=new ObjectInputStream(conexion.getInputStream());
        Carta cd= (Carta)in.readObject();
        return cd;
    }    
    
    //2
    public Carta enviarCarta(Mensaje mensaje) throws IOException, ClassNotFoundException
    {
        //mandar objeto
        mensaje.setIdMensaje(2);
        out.writeObject(mensaje);
        Object entrada=in.readObject();
        //ObjectInputStream in=new ObjectInputStream(conexion.getInputStream());
        if(entrada instanceof Carta)
        {
            System.out.println("es carta");
            Carta cd= (Carta)entrada;
            return cd;
        }//System.out.println(cd.toString());
        else 
        {
            System.out.println("no es carta");
            return null;
        }
        //out.flush();
        //out.close();
    }
    public void CerrarConexion() throws IOException
    {
        out.close();
        in.close();
        conexion.close();
        conexionReplicas.close();
    }

    public int getPUERTO() {
        return PUERTO;
    }

    public void setPUERTO(int PUERTO) {
        this.PUERTO = PUERTO;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public Socket getConexion() {
        return conexion;
    }

    public void setConexion(Socket conexion) {
        this.conexion = conexion;
    }

    public List getLista_ip() {
        return lista_ip;
    }

    public void setLista_ip(List lista_ip) {
        this.lista_ip = lista_ip;
    }

    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }

    public void setIP_ADDRESS(String IP_ADDRESS) {
        this.IP_ADDRESS = IP_ADDRESS;
    }

    public ObjectOutputStream getOut_replicas() {
        return out_replicas;
    }

    public void setOut_replicas(ObjectOutputStream out_replicas) {
        this.out_replicas = out_replicas;
    }

    public ObjectInputStream getIn_replicas() {
        return in_replicas;
    }

    public void setIn_replicas(ObjectInputStream in_replicas) {
        this.in_replicas = in_replicas;
    }

    public Socket getConexionReplicas() {
        return conexionReplicas;
    }

    public void setConexionReplicas(Socket conexionReplicas) {
        this.conexionReplicas = conexionReplicas;
    }
    
}
