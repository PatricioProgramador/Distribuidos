/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lamport;

import ConexionBD.Conexion_BD;
import Juego.Jugador;
import Sockets.Mensaje;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ale
 */
public class ConexionLamport extends Thread{
    private Socket con;
    private MensajeLamport mensaje;
    private ObjectInputStream flujo_entrada;
    private ObjectOutputStream flujo_salida;
    private Jugador cliente; 
    //private ClienteLamport lampC;
    
    public ConexionLamport(Socket con, Jugador cliente)
    {
        this.con=con;
        this.cliente=cliente;
        try{
            flujo_entrada= new ObjectInputStream(con.getInputStream());
            flujo_salida= new ObjectOutputStream(con.getOutputStream());
        }catch(IOException ex)
        {
            System.out.println("error en los stream ");
        }
    }    
    public void run()
    {
        boolean conectado=true;
        while(conectado)
        {
            try {
                mensaje=(MensajeLamport)flujo_entrada.readObject();
                
                //procesamiento del mensaje
                int val_cont=mensaje.getContador();
                if(val_cont>cliente.getContador())
                {
                    cliente.setContador(mensaje.getContador()+1);
                    val_cont=cliente.getContador();
                    val_cont=val_cont+cliente.getFactor();
                    cliente.setContador(val_cont);
                    //lampC.Enviar();
                    System.out.println("hora actua: "+ cliente.getContador());
                }
                else
                    System.out.println("hora actual: "+ cliente.getContador());

            } catch (IOException ex) {
                System.out.println("Cliente Desconectado");
                conectado=false;
                try {
                    flujo_entrada.close();
                    flujo_salida.close();
                } catch (IOException ex1) {System.out.println("error al cerrar los in/out");}            
            } catch (ClassNotFoundException ex) {Logger.getLogger(ConexionLamport.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }
}
