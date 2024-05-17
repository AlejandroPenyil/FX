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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ClientController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/login-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            LoginController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToJardines(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/jardin/jardines-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            JardinesController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToPresupuestos(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Presupuestos/presupuestos-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            PresupuestosController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToPeticiones(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Peticiones/peticiones-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            PeticionesController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToImagenes(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Imagenes/imagenes-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ImagenesController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToFacturas(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Facturas/facturas-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            FacturasController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
