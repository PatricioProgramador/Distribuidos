package Juego;

import Lamport.ClienteLamport;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

public class Jugador extends Thread{
    private int idJugador=2;
    private String host;
    private int contador=0;
    private int factor=10;
    private ArrayList<Carta> lista_cartas;
    private JTextField txt;
    public Jugador(String host) {
        this.host = host;
        lista_cartas=new ArrayList<>();
        start();   
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public ArrayList<Carta> getLista_cartas() {
        return lista_cartas;
    }

    public void setLista_cartas(ArrayList<Carta> lista_cartas) {
        this.lista_cartas = lista_cartas;
    }
    
    public void agregarCarta(Carta carta){
        this.lista_cartas.add(carta);
    }
    
    @Override
    public void run()
    {
        for(;;)
        {
            try {
                contador=contador+factor;
                txt.setText(contador+"");
                sleep(1000);
                //System.out.println(contador);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public JTextField getTxt() {
        return txt;
    }

    public void setTxt(JTextField txt) {
        this.txt = txt;
    }
    
}
