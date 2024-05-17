package com.example.fxtry.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private void goToClient(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/client-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ClientController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
