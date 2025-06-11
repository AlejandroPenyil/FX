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
    requires okhttp3;
    requires retrofit2.converter.gson;
    requires retrofit2;
    requires java.desktop;
    requires com.google.gson;

    opens com.example.fxtry to javafx.fxml;
    opens com.example.fxtry.Controller to javafx.fxml;
    opens com.example.fxtry.Controller.Create to javafx.fxml;
    opens com.example.fxtry.Controller.Update to javafx.fxml;
    opens com.example.fxtry.Model to com.google.gson;

    exports com.example.fxtry;
    exports com.example.fxtry.Controller;
    exports com.example.fxtry.Controller.Create;
    exports com.example.fxtry.Controller.Update;
    exports com.example.fxtry.Model;

}
