package Replicas;

import ConexionBD.Conexion_BD;
import static Sockets.Servidor.tope;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorReplicas extends Thread{
    private final int PUERTO=3005;
    private ServerSocket servidor;
    private ArrayList<Replica> replicas;
    public int soyPrincipal=0;
    public Conexion_BD cb;
    public ServidorReplicas() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        this.servidor=new ServerSocket(PUERTO);
        replicas=new ArrayList<>();
        cb=new Conexion_BD("localhost");
    }
    
    public void run()
    {
        while(true)
        {
            try {
                Socket con=servidor.accept();
                System.out.println("aceptado en serv replicas");
                ProcesarReplicas rep=new ProcesarReplicas(con);
                rep.start();
            } catch (IOException ex) {
                Logger.getLogger(ServidorReplicas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Boolean AgregarReplica( String ip_address, String esPrincipal/*, boolean principal*/)
    {
        try {
                //java.util.Date fecha = new Date();
                System.out.println(ip_address);
                Replica replica=new Replica(ip_address);
                if(esPrincipal.equals('1'))
                {
                    //replicas.remove(0); //eliminamos primero que es el que se toma como la replica
                    replicas.add(0, replica);
                    cb.InsertarReplica(ip_address, true);
                }
                else
                {
                    replicas.add(replica);
                    cb.InsertarReplica(ip_address, false);
                }
                //this.cb.InsertarReplica("", "ip_address", principal);
                //replica.CambiarTope(tope);
                //MensajeSQL mensaje= new MensajeSQL(6);
                //mensaje.setCarta(tope);
                
                return true;                
            } catch (IOException ex) {
                System.out.println("no existe la replica con la ip "+ ip_address);
                return false;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                //System.out.println("error de inicializacion");
                return false;
            } catch (SQLException ex) {
            Logger.getLogger(ServidorReplicas.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public ArrayList<Replica> getReplicas() {
        return replicas;
    }

    public void setReplicas(ArrayList<Replica> replicas) {
        this.replicas = replicas;
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        ServidorReplicas serv=new ServidorReplicas();
        serv.start();
    }
}
