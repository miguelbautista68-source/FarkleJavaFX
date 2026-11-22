package org.uabc.ic.farklejavafx.modelo;

// Jugador controlado por una persona.
public class JugadorHumano extends Jugador {

    // Crea una persona con nombre
    public JugadorHumano(String nombre) {
        super(nombre);
    }

    // Evalua si es un humano
    @Override
    public boolean esHumano() {
        return true;
    }
}