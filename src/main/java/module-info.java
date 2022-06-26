module br.edu.femass {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.datatransfer;
    requires lombok;
    requires java.desktop;

    opens br.edu.femass to javafx.fxml;
    exports br.edu.femass;
    exports br.edu.femass.gui;
    opens br.edu.femass.gui to javafx.fxml;
}