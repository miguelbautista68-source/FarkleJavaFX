package org.uabc.ic.farklejavafx.modelo;

// Clase base del dado
public class Dado {
    private int valor;
    private boolean seleccionado;
    private boolean apartado;

    public Dado() {
        seleccionado = false;
        apartado = false;
        tirar();
    }

    //Metodo para randomizar el dado
    public void tirar() {
        valor = (int) (Math.random() * 6) + 1;
    }

    public int getValor() {
        return valor;
    }

    public boolean estaSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public boolean estaApartado() {
        return apartado;
    }

    public void setApartado(boolean apartado) {
        this.apartado = apartado;
    }

    // Texto con el valor del dado y su estado actual
    @Override
    public String toString() {
        return "Dado de " + valor + ", seleccionado: " + seleccionado + ", apartado: " + apartado;
    }
}