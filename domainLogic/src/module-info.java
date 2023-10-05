module belegProg {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires org.jetbrains.annotations;

    opens administration to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports administration to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens cargo to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports cargo to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens cli to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports cli to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens gui to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports gui to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens gui.model to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports gui.model to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens gui.controller to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;
    exports gui.controller to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;

    opens io to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports io to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens management to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports management to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens management.events to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports management.observers to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens network.tcp to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports network.udp to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens simulations to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;
    exports simulations to javafx.graphics, javafx.fxml, javafx.base, javafx.controls;

    opens simulations.one to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;
    exports simulations.one to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;

    opens simulations.two to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;
    exports simulations.two to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;

    opens simulations.three to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;
    exports simulations.three to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;

}