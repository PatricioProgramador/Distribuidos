/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lamport;

import java.io.Serializable;

/**
 *
 * @author ale
 */
public class MensajeLamport implements Serializable{
    private int contador;
    public MensajeLamport(int contador) {
        this.contador = contador;
    }
    public int getContador() {
        return contador;
    }
    public void setContador(int contador) {
        this.contador = contador;
    }     
}
