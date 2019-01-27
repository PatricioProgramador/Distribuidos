package Replicas;

import ConexionBD.Conexion_BD;
import static Sockets.Servidor.tope;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import static practica5distr.GUIServidor.jLabel6;

public class ProcesarReplicas extends Thread{
    private Socket con;
    private Conexion_BD conBD;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public ProcesarReplicas(Socket con)
    {
        try {
            this.con=con;
            in=new ObjectInputStream(this.con.getInputStream());
            out=new ObjectOutputStream(this.con.getOutputStream());
            this.conBD=new Conexion_BD("localhost");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.out.println("error en ProcesarReplicas/constructor "+ex);
        } catch (IOException ex) {
            Logger.getLogger(ProcesarReplicas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    public void run()
    {
        boolean conectado=true;
        while(conectado)
        {
        try {
            //ObjectInputStream in=new ObjectInputStream(con.getInputStream());
            MensajeSQL sqlOperador=(MensajeSQL)in.readObject();
            int idMensaje=sqlOperador.getIdMensaje();
            System.out.println("id Mensaje"+idMensaje);
            switch(idMensaje)
            {
                case 1:
                    conBD.InsertarJugador(sqlOperador.getIdJugador(), sqlOperador.getHost());
                    break;
                case 2:
                    conBD.InsertarCartaJugador(sqlOperador.getIdJugador(), sqlOperador.getIdCarta());
                    break;
                case 3:
                    conBD.limpiarCartasJugadores();
                    break;
                case 4:
                    conBD.limpiarJugadores();
                    break;
                case 5:
                    conBD.EliminarCartaJugador(sqlOperador.getIdJugador(), sqlOperador.getIdCarta());
                    break;
                case 6:
                    tope=sqlOperador.getCarta();
                    Image image=ImageIO.read(new File(tope.getUrl()));
                    ImageIcon imageIcon = new ImageIcon(image);
                    jLabel6.setIcon(imageIcon);
                    break;
                case 7:
                    List resultados=conBD.BuscarReplicas();
                    System.out.println(resultados);
                    out.writeObject(resultados);
                    break;
                default:
                    break;
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            try {
                conBD.eliminarCon();
                conectado=false;
            } catch (SQLException ex1) {
                Logger.getLogger(ProcesarReplicas.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    }
}
