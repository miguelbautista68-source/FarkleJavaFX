package org.uabc.ic.farklejavafx.controlador;

import java.util.ArrayList;

import org.uabc.ic.farklejavafx.modelo.Dado;
import org.uabc.ic.farklejavafx.modelo.Jugador;
import org.uabc.ic.farklejavafx.modelo.JugadorComputadora;
import org.uabc.ic.farklejavafx.modelo.JugadorHumano;
import org.uabc.ic.farklejavafx.modelo.Puntuacion;

/**
 * Esta clase tiene el control del juego en base a las REGLAS. Contiene todos los combos que pueden hacerse.
 */
public class JuegoFarkle {

    private static final int CANTIDAD_DADOS = 6;
    private static final int PUNTOS_PARA_ENTRAR = 200;
    private static final int PUNTOS_PARA_GANAR = 2000;

    private ArrayList<Jugador> jugadores;
    private ArrayList<Dado> dados;
    private ArrayList<String> combosTurno;

    private int turnoActual;
    private int puntosTurno;
    private boolean tiradaPendiente;
    private boolean ultimaRonda;
    private int jugadorQueLlegoAMeta;
    private boolean juegoTerminado;
    private String mensaje;

    //Crea el juego inicial con las personas y bots, y planta los 6 dados
    public JuegoFarkle(int humanos, int computadoras) {
        jugadores = new ArrayList<>();
        dados = new ArrayList<>();
        combosTurno = new ArrayList<>();

        for (int i = 1; i <= humanos; i++) {
            jugadores.add(new JugadorHumano("Jugador " + i));
        }

        for (int i = 1; i <= computadoras; i++) {
            jugadores.add(new JugadorComputadora("Bot " + i));
        }

        //Aqui es donde se agrega un bot automaticamente si es que solo se eligio un jugador
        int totalComputadoras = computadoras;
        while (jugadores.size() < 2) {
            totalComputadoras = totalComputadoras + 1;
            jugadores.add(new JugadorComputadora("Bot " + totalComputadoras));
        }

        for (int i = 0; i < CANTIDAD_DADOS; i++) {
            dados.add(new Dado());
        }

        turnoActual = 0;
        puntosTurno = 0;
        tiradaPendiente = false;
        ultimaRonda = false;
        jugadorQueLlegoAMeta = -1;
        juegoTerminado = false;
        mensaje = "Turno de " + jugadorActual().getNombre() + ".";
    }

    //==== Aqui es donde se administran las acciones que puede hacer el jugador

    //Aqui se tiran los dados que no esten apartados
    public void tirar() {
        for (Dado dado : dados) {
            if (dado.estaApartado() == false) {
                dado.tirar();
            }
        }
        tiradaPendiente = true;

        if (Puntuacion.hayPuntos(valoresDisponibles()) == false) {
            mensaje = "¡FARKLE! " + jugadorActual().getNombre()
                    + " pierde los " + puntosTurno + " puntos del turno.";
            pasarTurno();
        }
    }

    //Aqui es donde se marca o se desmarca un dado para apartarlo
    public void seleccionarDado(int indice) {
        Dado dado = dados.get(indice);

        if (tiradaPendiente == false) {
            return;
        }
        if (dado.estaApartado() == true) {
            return;
        }

        if (dado.estaSeleccionado() == true) {
            dado.setSeleccionado(false);
        } else {
            dado.setSeleccionado(true);
        }
    }

    //Aqui es donde se apartan los dados seleccionados y se suman sus puntos
    public boolean apartarSeleccion() {
        ArrayList<Integer> valores = valoresSeleccionados();

        if (valores.size() == 0) {
            mensaje = "Tienes que seleccionar al menos un dado que valga puntos.";
            return false;
        }

        int puntos = Puntuacion.calcular(valores);
        if (puntos == 0) {
            mensaje = "Hay dados seleccionados que no valen puntos.";
            return false;
        }

        puntosTurno = puntosTurno + puntos;
        combosTurno.add(Puntuacion.describir(valores) + " (" + puntos + ")");
        mensaje = "Combos: " + String.join(", ", combosTurno);

        for (Dado dado : dados) {
            if (dado.estaSeleccionado() == true) {
                dado.setApartado(true);
                dado.setSeleccionado(false);
            }
        }
        tiradaPendiente = false;

        if (todosApartados() == true) {
            limpiarDados();
            mensaje = mensaje + " Los 6 dados valen puntos, puedes tirar los 6 otra vez.";
        }

        return true;
    }

    // Guarda los puntos del turno en el marcador y pasa el turno
    public boolean plantarse() {
        Jugador jugador = jugadorActual();

        if (puntosTurno == 0) {
            mensaje = "Primero tira los dados.";
            return false;
        }

        if (jugador.estaEnJuego() == false && puntosTurno < PUNTOS_PARA_ENTRAR) {
            mensaje = "Necesitas al menos " + PUNTOS_PARA_ENTRAR
                    + " puntos en un turno para tirar por primera vez.";
            return false;
        }

        jugador.sumarPuntos(puntosTurno);
        mensaje = jugador.getNombre() + " anota " + puntosTurno + " puntos.";

        if (jugador.getPuntos() >= PUNTOS_PARA_GANAR && ultimaRonda == false) {
            ultimaRonda = true;
            jugadorQueLlegoAMeta = turnoActual;
            mensaje = mensaje + " Llego a " + PUNTOS_PARA_GANAR
                    + ", Los demás tienen un último turno.";
        }

        pasarTurno();
        return true;
    }

    //===== Metodos del bot, cada llamada hace un paso.

    //Aqui donde se decide si tira o aparta dados que valen, y decide si se planta o no (siendo esta como el metodo principal del bot)
    public void jugarPasoComputadora() {
        if (juegoTerminado == true) {
            return;
        }

        if (tiradaPendiente == false) {
            tirar();
            return;
        }

        seleccionarDadosQueValenPuntos();
        apartarSeleccion();

        if (debePlantarse() == true) {
            plantarse();
        }
    }

    // El bot selecciona los dados que valen puntos
    private void seleccionarDadosQueValenPuntos() {
        ArrayList<Integer> valores = valoresDisponibles();
        int[] conteo = Puntuacion.contar(valores);
        boolean todosValen = Puntuacion.calcular(valores) > 0;

        for (Dado dado : dados) {
            if (dado.estaApartado() == false) {
                int valor = dado.getValor();

                if (todosValen == true) {
                    dado.setSeleccionado(true);
                } else if (valor == 1 || valor == 5 || conteo[valor] >= 3) {
                    dado.setSeleccionado(true);
                }
            }
        }
    }

    // El bot decide si ya se planta o sigue tirando
    private boolean debePlantarse() {
        Jugador jugador = jugadorActual();
        int total = jugador.getPuntos() + puntosTurno;

        // Todavía no puede anotar, tiene que seguir tirando
        if (jugador.estaEnJuego() == false && puntosTurno < PUNTOS_PARA_ENTRAR) {
            return false;
        }

        // En la ultima ronda el bot sigue hasta perder o tener una puntuacion ganadora 
        if (ultimaRonda == true) {
            if (total > jugadorConMasPuntos().getPuntos()) {
                return true;
            }
            return false;
        }

        if (total >= PUNTOS_PARA_GANAR) {
            return true;
        }
        if (puntosTurno >= 500) {
            return true;
        }
        if (valoresDisponibles().size() <= 2 && puntosTurno >= 300) {
            return true;
        }

        return false;
    }

    // Otros metodos (no tienen que ver con los bots o las personas)

    //Termina el turno y limpia los dados, avanza al siguiente jugador y finalmente revisa si se acabo el juego
    private void pasarTurno() {
        puntosTurno = 0;
        tiradaPendiente = false;
        combosTurno.clear();
        limpiarDados();

        turnoActual = (turnoActual + 1) % jugadores.size();

        if (ultimaRonda == true && turnoActual == jugadorQueLlegoAMeta) {
            juegoTerminado = true;
            Jugador ganador = jugadorConMasPuntos();
            mensaje = mensaje + " FIN DEL JUEGO. Gana " + ganador.getNombre()
                    + " con " + ganador.getPuntos() + " puntos.";
            return;
        }

        mensaje = mensaje + " Turno de " + jugadorActual().getNombre() + ".";
    }

    //Aqui es donde se quitan lo "seleccionado" de los dados
    private void limpiarDados() {
        dados.forEach(dado -> {
            dado.setApartado(false);
            dado.setSeleccionado(false);
        });
    }

    //Ordena a los jugadores por puntos, para mostrar primero al de mayor puntaje y al ultimo con menor puntaje
    private Jugador jugadorConMasPuntos() {
        ArrayList<Jugador> ordenados = new ArrayList<>(jugadores);
        ordenados.sort((jugadorA, jugadorB) -> {
            return jugadorB.getPuntos() - jugadorA.getPuntos();
        });
        return ordenados.get(0);
    }

    //Administra los valores que todavia no se han apartado
    private ArrayList<Integer> valoresDisponibles() {
        ArrayList<Integer> valores = new ArrayList<>();
        for (int i = 0; i < dados.size(); i++) {
            Dado dado = dados.get(i);
            if (dado.estaApartado() == false) {
                valores.add(dado.getValor());
            }
        }
        return valores;
    }

    //Administra los valores que estan seleccionados
    private ArrayList<Integer> valoresSeleccionados() {
        ArrayList<Integer> valores = new ArrayList<>();
        for (int i = 0; i < dados.size(); i++) {
            Dado dado = dados.get(i);
            if (dado.estaSeleccionado() == true) {
                valores.add(dado.getValor());
            }
        }
        return valores;
    }

    //Regresa true si los 6 dados ya estan apartados
    private boolean todosApartados() {
        for (int i = 0; i < dados.size(); i++) {
            Dado dado = dados.get(i);
            if (dado.estaApartado() == false) {
                return false;
            }
        }
        return true;
    }

    // getters y setters

    public Jugador jugadorActual() {
        return jugadores.get(turnoActual);
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public ArrayList<Dado> getDados() {
        return dados;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public int getPuntosTurno() {
        return puntosTurno;
    }

    public boolean tiradaEstaPendiente() {
        return tiradaPendiente;
    }

    public boolean juegoEstaTerminado() {
        return juegoTerminado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getCombosPosibles() {
        if (tiradaPendiente == false) {
            return "Tira los dados para ver tus combos disponibles";
        }
        return Puntuacion.combosPosibles(valoresDisponibles());
    }
}