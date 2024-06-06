package com.example.fxtry.Controller.Update;

import com.example.fxtry.Controller.ClientController;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.fxtry.Controller.ClientController.updatable;

public class ClientUpdateController {

    private ImplRetroFit implRetroFit;

    @FXML
    private TextField txtName, txtDireccion, txtCorreo, txtTelefono, txtApellidos;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();
        txtName.setText(updatable.getNombre());
        txtDireccion.setText((updatable.getDireccion()));
        txtApellidos.setText((updatable.getApellidos()));
        txtCorreo.setText((updatable.getCorreo()));
    }

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
    private void update(ActionEvent event) throws IOException {
        try {
            updatable.setNombre(txtName.getText());
            updatable.setDireccion(txtDireccion.getText());
            updatable.setApellidos(txtApellidos.getText());
            updatable.setCorreo(txtCorreo.getText());

            implRetroFit.putUsuario(updatable);

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
}
