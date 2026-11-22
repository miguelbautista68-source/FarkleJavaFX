package org.uabc.ic.farklejavafx.vista;

import java.util.ArrayList;

import org.uabc.ic.farklejavafx.controlador.JuegoFarkle;
import org.uabc.ic.farklejavafx.modelo.Dado;
import org.uabc.ic.farklejavafx.modelo.Jugador;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Vista no ejecuta reglas ni nada de eso.
 * Tiene dos pantallas dentro de la misma ventana, la de elegir jugadores y la de elegir bots
 */
public class VistaFarkle {

    private static final String[] CARAS = {"1", "2", "3", "4", "5", "6"};

    private static final String ESTILO_BASE = "-fx-font-size: 56px; -fx-border-color: black; "
            + "-fx-border-radius: 10; -fx-background-radius: 10; ";
    private static final String ESTILO_NORMAL = ESTILO_BASE + "-fx-background-color: white;";
    private static final String ESTILO_SELECCIONADO = ESTILO_BASE + "-fx-background-color: gold;";
    private static final String ESTILO_APARTADO = ESTILO_BASE
            + "-fx-background-color: lightgray; -fx-opacity: 0.6;";

    // Lista fija de los combos del Farkle para tenerla a la mano
    private static final String REGLAS_COMBOS = "Un 1 = 100 puntos (cada uno)\n"
            + "Un 5 = 50 puntos (cada uno)\n"
            + "Tres 1s = 300 puntos\n"
            + "Tres 2s = 200 puntos\n"
            + "Tres 3s = 300 puntos\n"
            + "Tres 4s = 400 puntos\n"
            + "Tres 5s = 500 puntos\n"
            + "Tres 6s = 600 puntos\n"
            + "Cuatro iguales = 1000 puntos\n"
            + "Cinco iguales = 2000 puntos\n"
            + "Seis iguales = 3000 puntos\n"
            + "Escalera (1 al 6) = 1500 puntos\n"
            + "Tres pares = 1500 puntos\n"
            + "Cuatro iguales + par = 1500 puntos\n"
            + "Dos tercias = 2500 puntos\n"
            + "\n"
            + "Si ningún dado vale, es farkle (se pasa el turno y pierdes puntos)";

    private JuegoFarkle juego;
    private BorderPane raiz;

    // Estas son las variables de la pantalla de seleccion de jugadores y bots
    private Label etiquetaPregunta;
    private ArrayList<Button> botonesSeleccion;

    // Estas son las variables de la pantalla del juego normal
    private Label etiquetaMensaje;
    private Label etiquetaTurno;
    private Label etiquetaCombos;
    private ArrayList<Button> botonesDados;
    private ArrayList<Label> etiquetasJugadores;
    private Button botonTirar;
    private Button botonPlantarse;

    // Crea la ventana principal y muestra la pantalla de selección
    public VistaFarkle() {
        raiz = new BorderPane();
        crearPantallaSeleccion();
    }

    // Esta sección es de la pantalla de seleccion, donde eliges o humanos o bots

    // Arma la pantalla de selección: la pregunta y los botones del 0 al 4
    private void crearPantallaSeleccion() {
        etiquetaPregunta = new Label();
        etiquetaPregunta.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        botonesSeleccion = new ArrayList<>();
        HBox cajaBotones = new HBox(15);
        cajaBotones.setAlignment(Pos.CENTER);
        for (int i = 0; i <= 4; i++) {
            Button boton = new Button(String.valueOf(i));
            boton.setPrefSize(80, 80);
            boton.setStyle("-fx-font-size: 28px;");
            botonesSeleccion.add(boton);
            cajaBotones.getChildren().add(boton);
        }

        VBox cajaSeleccion = new VBox(30, etiquetaPregunta, cajaBotones);
        cajaSeleccion.setAlignment(Pos.CENTER);
        raiz.setCenter(cajaSeleccion);
    }

    // Cambia el texto de la pregunta de la pantalla de selección
    public void mostrarPregunta(String texto) {
        etiquetaPregunta.setText(texto);
    }

    //Esta sección es la del juego

    // Aqui se cambia la pantalla (la del menu de elegir jugadores y bots a lo del juego)
    public void mostrarJuego(JuegoFarkle juego) {
        this.juego = juego;

        etiquetaTurno = new Label();
        etiquetaTurno.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        etiquetaMensaje = new Label();
        etiquetaMensaje.setStyle("-fx-font-size: 15px;");
        etiquetaMensaje.setWrapText(true);

        VBox cajaArriba = new VBox(8, etiquetaTurno, etiquetaMensaje);
        cajaArriba.setPadding(new Insets(15));

        botonesDados = new ArrayList<>();
        HBox cajaDados = new HBox(12);
        cajaDados.setAlignment(Pos.CENTER);
        for (int i = 0; i < juego.getDados().size(); i++) {
            Button boton = new Button();
            boton.setPrefSize(95, 95);
            botonesDados.add(boton);
            cajaDados.getChildren().add(boton);
        }

        //Seccion de puntaje
        etiquetasJugadores = new ArrayList<>();
        VBox cajaPuntajes = new VBox(10);
        cajaPuntajes.setPadding(new Insets(15));
        cajaPuntajes.setMinWidth(190);
        Label titulo = new Label("Puntajes");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        cajaPuntajes.getChildren().add(titulo);
        for (int i = 0; i < juego.getJugadores().size(); i++) {
            Label etiqueta = new Label();
            etiqueta.setStyle("-fx-font-size: 15px;");
            etiquetasJugadores.add(etiqueta);
            cajaPuntajes.getChildren().add(etiqueta);
        }

        //Seccion de combos
        Label tituloCombos = new Label("Combos del juego");
        tituloCombos.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label etiquetaReglas = new Label(REGLAS_COMBOS);
        etiquetaReglas.setStyle("-fx-font-size: 13px;");

        Label tituloPosibles = new Label("Con tus dados puedes:");
        tituloPosibles.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        etiquetaCombos = new Label();
        etiquetaCombos.setStyle("-fx-font-size: 13px;");
        etiquetaCombos.setWrapText(true);

        VBox cajaCombos = new VBox(8, tituloCombos, etiquetaReglas, tituloPosibles, etiquetaCombos);
        cajaCombos.setPadding(new Insets(15));
        cajaCombos.setMinWidth(260);
        cajaCombos.setMaxWidth(260);

        //Seccion de botones inferiores
        botonTirar = new Button("Tirar");
        botonPlantarse = new Button("Plantarse");
        botonTirar.setStyle("-fx-font-size: 18px;");
        botonPlantarse.setStyle("-fx-font-size: 18px;");
        HBox cajaBotones = new HBox(20, botonTirar, botonPlantarse);
        cajaBotones.setAlignment(Pos.CENTER);
        cajaBotones.setPadding(new Insets(20));

        raiz.setTop(cajaArriba);
        raiz.setLeft(cajaPuntajes);
        raiz.setCenter(cajaDados);
        raiz.setRight(cajaCombos);
        raiz.setBottom(cajaBotones);
    }

    // Lee el modelo y refresca todo lo que se ve
    public void actualizar() {
        ArrayList<Dado> dados = juego.getDados();
        for (int i = 0; i < dados.size(); i++) {
            Dado dado = dados.get(i);
            Button boton = botonesDados.get(i);

            boton.setText(CARAS[dado.getValor() - 1]);

            if (dado.estaApartado() == true) {
                boton.setStyle(ESTILO_APARTADO);
            } else if (dado.estaSeleccionado() == true) {
                boton.setStyle(ESTILO_SELECCIONADO);
            } else {
                boton.setStyle(ESTILO_NORMAL);
            }
        }

        ArrayList<Jugador> jugadores = juego.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            String marca = "   ";
            if (i == juego.getTurnoActual() && juego.juegoEstaTerminado() == false) {
                marca = "-> ";
            }
            etiquetasJugadores.get(i).setText(marca + jugador.getNombre() + ": " + jugador.getPuntos());
        }

        if (juego.juegoEstaTerminado() == true) {
            etiquetaTurno.setText("Juego terminado");
        } else {
            etiquetaTurno.setText("Turno de " + juego.jugadorActual().getNombre()
                    + "  |  Puntos del turno: " + juego.getPuntosTurno());
        }
        etiquetaMensaje.setText(juego.getMensaje());
        etiquetaCombos.setText(juego.getCombosPosibles());

        boolean turnoDeHumano = juego.jugadorActual().esHumano() && juego.juegoEstaTerminado() == false;
        botonTirar.setDisable(turnoDeHumano == false);
        botonPlantarse.setDisable(turnoDeHumano == false);
    }

    // Espera 1.2 segundos y luego ejecuta lo que le pasen
    public void esperarPaso(Runnable accion) {
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.2));
        pausa.setOnFinished(evento -> {
            accion.run();
        });
        pausa.play();
    }

    //getters

    public BorderPane getRaiz() {
        return raiz;
    }

    public ArrayList<Button> getBotonesSeleccion() {
        return botonesSeleccion;
    }

    public ArrayList<Button> getBotonesDados() {
        return botonesDados;
    }

    public Button getBotonTirar() {
        return botonTirar;
    }

    public Button getBotonPlantarse() {
        return botonPlantarse;
    }
}