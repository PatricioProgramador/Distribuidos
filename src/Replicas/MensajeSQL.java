package Replicas;

import Juego.Carta;
import java.io.Serializable;

public class MensajeSQL implements Serializable{
    private int idMensaje;
    private int idJugador;
    private int idCarta;
    private String host;
    private Carta carta;
    
    //inserción y modificación
    public MensajeSQL(int idMensaje, int idJugador, int idCarta) {
        this.idMensaje = idMensaje;
        this.idJugador= idJugador;
        this.idCarta= idCarta;
    }
    //registrar usuario
    public MensajeSQL(int idMensaje, int idJugador, String host)
    {
        this.idMensaje = idMensaje;
        this.idJugador= idJugador;
        this.host=host;
    }
    //limpiar
    public MensajeSQL(int idMensaje)
    {
        this.idMensaje = idMensaje;
    }
    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public int getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(int idCarta) {
        this.idCarta = idCarta;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }    
}
