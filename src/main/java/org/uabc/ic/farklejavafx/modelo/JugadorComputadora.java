package org.uabc.ic.farklejavafx.modelo;


// Bot que revise la hrerencia del jugador.
public class JugadorComputadora extends Jugador {

    // Crea un bot con nombre
    public JugadorComputadora(String nombre) {
        super(nombre);
    }

    // Evalua si es humano o no
    @Override
    public boolean esHumano() {
        return false;
    }
}