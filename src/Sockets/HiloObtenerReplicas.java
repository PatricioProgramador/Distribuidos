/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import Lamport.ClienteLamport;
import Lamport.MensajeLamport;
import Replicas.MensajeSQL;
import Replicas.ServidorReplicas;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static practica5distr.GUICliente.cl;
import java.util.List;
/**
 *
 * @author ale
 */
public class HiloObtenerReplicas extends Thread{
    public Socket con;
    public ObjectOutputStream out_replicas;
    public ObjectInputStream in_replicas;
    public Cliente cliente;
    public HiloObtenerReplicas(Cliente cliente) throws IOException {
        this.cliente=cliente;
        this.con=cliente.getConexionReplicas();
        System.out.println("xxxxxx");
        this.out_replicas=cliente.getOut_replicas();
        this.in_replicas=cliente.getIn_replicas();
        System.out.println("xxxxxx");
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try {
                    sleep(1000);
                    MensajeSQL mensaje=new MensajeSQL(7);
                    out_replicas.writeObject(mensaje);
                    //System.out.println(out_replicas);
                    cliente.setLista_ip((List)in_replicas.readObject()); 
                    System.out.println(cliente.getLista_ip());
            } catch (IOException | InterruptedException ex) {
                try {
                    //error para detectar que no está disponible el servidor
                    System.out.println("asignar ip ");
                    /*out_replicas.close();
                    in_replicas.close();
                    con.close();
                    */
                    //String nueva_ip=ObtenerIpLista();
                    //System.out.println(nueva_ip);
                    //miriam 8.120.0
                    modificarConexion("8.120.0.123");
                    //modificarConexion("8.120.0.161");
                    break;
                } catch (IOException | ClassNotFoundException ex1) {
                    System.out.println(ex);
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HiloObtenerReplicas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
    }
    public void modificarConexion(String ip) throws IOException, ClassNotFoundException{
        /*cliente.CerrarConexion();
        cliente=null;*/
        cl=new Cliente(ip);
    }

    private String ObtenerIpLista() {
        return (String) cliente.lista_ip.get(0);
    }
   
}
