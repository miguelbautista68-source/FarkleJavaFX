package org.uabc.ic.farklejavafx.modelo;

//import com.farkle.Modelo.Jugador;

public class JugadorComputadora extends Jugador {

        public JugadorComputadora(String nombre) {
            super(nombre);
        }

        @Override
        public boolean esHumano() {
            return false;
        }
}
