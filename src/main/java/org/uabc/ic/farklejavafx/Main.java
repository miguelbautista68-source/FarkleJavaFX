package org.uabc.ic.farklejavafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.uabc.ic.farklejavafx.controlador.ControladorFarkle;
import org.uabc.ic.farklejavafx.vista.VistaFarkle;

public class Main extends Application {

    @Override
    public void start(Stage ventana) {
        VistaFarkle vista = new VistaFarkle();
        new ControladorFarkle(vista);

        ventana.setTitle("Farkle");
        ventana.setScene(new Scene(vista.getRaiz(), 850, 480));
        ventana.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}