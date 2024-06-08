package com.example.fxtry.Controller.Create;

import com.example.fxtry.Controller.ClientController;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientCreateController {

    private ImplRetroFit implRetroFit;

    @FXML
    private TextField txtNombre, txtDireccion, txtCorreo, txtTelefono, txtApellidos, txtDni, txtContraseña, txtUsuario;
    
    @FXML
    private ComboBox<String> cmbRol;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        cmbRol.getItems().addAll("ADMIN", "CLIENT", "GUEST");
        cmbRol.getSelectionModel().selectFirst();
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
    private void create(ActionEvent event) throws IOException {
        try {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNombre(txtNombre.getText());
            usuarioDTO.setApellidos(txtApellidos.getText());
            usuarioDTO.setCorreo(txtCorreo.getText());
            usuarioDTO.setTelefono(txtTelefono.getText());
            usuarioDTO.setDni(txtDni.getText());
            usuarioDTO.setContraseña(txtContraseña.getText());
            usuarioDTO.setRol(cmbRol.getValue());
            usuarioDTO.setUserName(txtUsuario.getText());
            usuarioDTO.setDireccion(txtDireccion.getText());

            implRetroFit.usarioCreate(usuarioDTO);

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
