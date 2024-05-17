module com.example.fxtry {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;

    opens com.example.fxtry to javafx.fxml;
    opens com.example.fxtry.Controller to javafx.fxml;
    opens com.example.fxtry.Controller.Create to javafx.fxml;
    opens com.example.fxtry.Controller.Update to javafx.fxml;
    opens com.example.fxtry.Model to java.base;

    exports com.example.fxtry;
    exports com.example.fxtry.Controller;
    exports com.example.fxtry.Controller.Create;
    exports com.example.fxtry.Controller.Update;
}