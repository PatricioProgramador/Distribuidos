package Sockets;

import Sockets.Servidor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Contador extends Thread{
        private Servidor ser;

        public Contador(Servidor ser) {
            this.ser = ser;
        }
        
        public void run()
        {
            for(;;)
            {
                System.out.println("En la pila hay: "+ser.getClientes().size());
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Contador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }