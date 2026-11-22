module org.uabc.ic.farklejavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.uabc.ic.farklejavafx to javafx.fxml;
    exports org.uabc.ic.farklejavafx;
}