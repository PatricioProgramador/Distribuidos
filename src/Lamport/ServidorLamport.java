package Lamport;

import Juego.Jugador;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorLamport extends Thread{
    private ServerSocket servidor;
    private int puerto=3001;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private MensajeLamport mensaje;
    private Jugador cliente;
    public ServidorLamport(Jugador cliente) throws IOException {
        this.cliente=cliente;
        servidor= new ServerSocket(puerto, 1);   
    }

    public void run()
    {
        boolean conectado=true;
        while(conectado)
            {
                System.out.println("Esperando");
                try {
                    //se acepta la conexión
                    Socket con=servidor.accept();
                    
                    ConexionLamport clamp= new ConexionLamport(con,cliente);
                    clamp.run();
                    
                } catch (IOException ex) {
                    System.out.println("Cliente Desconectado");
                    conectado=false;
                        try {
                            in.close();
                            out.close();
                        } catch (IOException ex1) {
                            Logger.getLogger(ServidorLamport.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                }
            }
    }
    /*public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println("sevidor");
        Jugador jug=new Jugador("55.6.99.54");
        jug.setFactor(3);
        
        ServidorLamport serv=new ServidorLamport(jug);
        serv.start();
        
    }*/
}
