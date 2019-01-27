/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import ConexionBD.Conexion_BD;
import Juego.Carta;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import static practica5distr.GUIServidor.jLabel6;
import static practica5distr.GUIServidor.jTextField1;

/**
 *
 * @author ale
 */
public class Servidor extends Thread{
    private static final int PUERTO=3000;
    ObjectOutputStream out;
    ServerSocket servidor;
    ObjectInputStream in;
    Mensaje mensaje;
    private final int maximoConexiones=3;
    public static ArrayList<Conexion_Cliente> clientes;
    private Conexion_BD conBD;
    private List lista_cartas;
    public static Carta tope;
    Contador cont;
    
    public Servidor() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
        //abrir un escuchador de servidor
        servidor = new ServerSocket(PUERTO, maximoConexiones);
        clientes= new ArrayList<>();
        conBD=new Conexion_BD("localhost");
        lista_cartas=conBD.ObtenerListaCartas();
        conBD.eliminarCon();
        
        tope=ObtenerAleatorio();
        jTextField1.setText(tope.getValor()+","+tope.getTipo());
        
        Image image=ImageIO.read(new File(tope.getUrl()));
        ImageIcon imageIcon = new ImageIcon(image);
        jLabel6.setIcon(imageIcon);
    }
    public void run()
    {
        while(true)
            {
                
                System.out.println("Esperando");
                try {
                    //se acepta la conexión http://www.webtutoriales.com/articulos/comunicacion-entre-un-servidor-y-multiples-clientes
                    Socket con=servidor.accept();
                    clientes.add(new Conexion_Cliente(con, mensaje, lista_cartas));
                    ExecutorService executor=Executors.newFixedThreadPool(1);
                    for(int i=0;i<clientes.size();i++)
                        executor.execute(clientes.get(i));
                    executor.shutdown();
                } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    

    public Contador getCont() {
        return cont;
    }

    public void setCont(Contador cont) {
        this.cont = cont;
    }

    public ArrayList<Conexion_Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Conexion_Cliente> clientes) {
        this.clientes = clientes;
    }

    public Conexion_BD getConBD() {
        return conBD;
    }

    public void setConBD(Conexion_BD conBD) {
        this.conBD = conBD;
    }
    
    private Carta ObtenerAleatorio()
    {
        int indice=lista_cartas.size();
        System.out.println(indice);
        int aleatorio= (int)(Math.random()*indice);
        Carta x=(Carta)lista_cartas.get(aleatorio);
        lista_cartas.remove(aleatorio);
        return x;
    }
}
