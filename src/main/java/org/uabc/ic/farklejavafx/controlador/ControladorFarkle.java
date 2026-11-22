package org.uabc.ic.farklejavafx.controlador;

import org.uabc.ic.farklejavafx.vista.VistaFarkle;

/**
 * Conecta la vista con el modelo, aqui recibe los clic y vista se actualiza.
 * ESTA CLASE lo que hace es administrar los turnos, le da indicaciones a los botones (en vista) y acomoda a los bots o personas que jueguen
 */
public class ControladorFarkle {

    private JuegoFarkle juego;
    private VistaFarkle vista;
    private int cantidadBots;
    private boolean botsElegidos;

    // Guarda la vista, pregunta cuántos bots van a jugar y conecta los botones de selección
    // Guarda la vista y pregunta cuantos bots jugaran, luego conecta los botones de seleccion
    public ControladorFarkle(VistaFarkle vista) {
        this.vista = vista;
        this.juego = null;
        this.botsElegidos = false;

        vista.mostrarPregunta("Cuantos bots van a jugar?");
        conectarBotonesSeleccion();
    }

    //===== Aqui se selecciionan de bots y personas

    //Cada boton de seleccion manda su numero al controlador (los botones que se muestran en pantalla)
    private void conectarBotonesSeleccion() {
        for (int i = 0; i < vista.getBotonesSeleccion().size(); i++) {
            int cantidad = i;
            vista.getBotonesSeleccion().get(i).setOnAction(evento -> {
                alElegirCantidad(cantidad);
            });
        }
    }

    // Antes se eligieron la cantidad de bots, ahora se eligen la cantidad de personas
    private void alElegirCantidad(int cantidad) {
        if (botsElegidos == false) {
            cantidadBots = cantidad;
            botsElegidos = true;
            vista.mostrarPregunta("Cuantas personas van a jugar?");
            return;
        }

        empezarJuego(cantidad);
    }

    //Crea el juego con las personas elegidas (bots y personas), cambia la pantlla y empieza el turno
    private void empezarJuego(int personas) {
        juego = new JuegoFarkle(personas, cantidadBots);
        vista.mostrarJuego(juego);
        conectarEventosJuego();
        vista.actualizar();
        revisarTurnoComputadora();
    }

    // === Desde aqui empieza el juego

    //Cada boton ejecuta su metodo cuando se hace clic (como el getBotonTirar, hace que se tiren los dados, el getBotonPlantarse hace que te plantes, etc)
    private void conectarEventosJuego() {
        vista.getBotonTirar().setOnAction(evento -> {
            alPresionarTirar();
        });

        vista.getBotonPlantarse().setOnAction(evento -> {
            alPresionarPlantarse();
        });

        for (int i = 0; i < vista.getBotonesDados().size(); i++) {
            int indice = i;
            vista.getBotonesDados().get(i).setOnAction(evento -> {
                alPresionarDado(indice);
            });
        }
    }

    // Selecciona o desmarca el dado al que se le dio clic (solo si juega una persona)
    private void alPresionarDado(int indice) {
        if (juego.jugadorActual().esHumano() == false) {
            return;
        }

        juego.seleccionarDado(indice);
        vista.actualizar();
    }

    //Aparte los dados que hayas seleccionado y tira el resto. (esto es cuando se seleccionan dados se tiran otros)
    private void alPresionarTirar() {
        if (juego.jugadorActual().esHumano() == false) {
            return;
        }

        if (juego.tiradaEstaPendiente() == true) {
            boolean seApartaron = juego.apartarSeleccion();
            if (seApartaron == false) {
                vista.actualizar();
                return;
            }
        }

        juego.tirar();
        vista.actualizar();
        revisarTurnoComputadora();
    }

    //Igual al anterior pero con los puntos, guarda los puntos de tus dados seleccionados
    private void alPresionarPlantarse() {
        if (juego.jugadorActual().esHumano() == false) {
            return;
        }

        if (juego.tiradaEstaPendiente() == true) {
            boolean seApartaron = juego.apartarSeleccion();
            if (seApartaron == false) {
                vista.actualizar();
                return;
            }
        }

        juego.plantarse();
        vista.actualizar();
        revisarTurnoComputadora();
    }

    //Si juega la computadora, este metodo hace que se tarde un poco más para poder visualizarlo
    private void revisarTurnoComputadora() {
        if (juego.juegoEstaTerminado() == true) {
            return;
        }
        if (juego.jugadorActual().esHumano() == true) {
            return;
        }

        vista.esperarPaso(() -> {
            juego.jugarPasoComputadora();
            vista.actualizar();
            revisarTurnoComputadora();
        });
    }
}