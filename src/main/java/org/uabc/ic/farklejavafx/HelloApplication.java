package org.uabc.ic.farklejavafx;

import org.uabc.ic.farklejavafx.controlador.ControladorFarkle;
import org.uabc.ic.farklejavafx.vista.VistaFarkle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Clase principal del JavaFx, arma la ventana y conecta la vista con el controlador
public class HelloApplication extends Application {

    @Override
    public void start(Stage ventana) {
        VistaFarkle vista = new VistaFarkle();
        new ControladorFarkle(vista);

        ventana.setTitle("Farkle");
        ventana.setScene(new Scene(vista.getRaiz(), 1150, 560));
        ventana.show();
    }
}