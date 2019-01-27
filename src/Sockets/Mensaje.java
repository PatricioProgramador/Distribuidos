package Sockets;

import Juego.Carta;
import java.io.Serializable;

public class Mensaje implements Serializable{
    private String ip;
    private int hr;
    private int idJugador;
    private int idMensaje;
    private Carta carta;
    public Mensaje(int idJugador, String ip, int hr) {
        this.idJugador=idJugador;
        this.ip=ip;
        this.hr = hr;
    }
    public Mensaje(int idMensaje)
    {
        this.idMensaje=idMensaje;
    }
    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }
    
    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }
    
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }
    
}
