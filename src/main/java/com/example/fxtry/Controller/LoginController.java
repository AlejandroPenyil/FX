package com.example.fxtry.Controller;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    public static UsuarioDTO admin = new UsuarioDTO();

    private ImplRetroFit implRetroFit;

    @FXML
    private ImageView imgPortrait;

    @FXML
    private TextField lblName;

    @FXML
    private Label lblError;

    @FXML
    private PasswordField lblPasword;

    @FXML
    private void initialize(){
        implRetroFit = new ImplRetroFit();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        imgPortrait.setImage(image);
    }

    @FXML
    private void goToMain(ActionEvent event) {
        try {
            lblError.setVisible(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            // Acceso al controlador de la segunda escena, si es necesario
            MainController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            LogginRequest loginRequest = new LogginRequest();
            loginRequest.setUserName(lblName.getText().trim());
            loginRequest.setContraseña(lblPasword.getText());

            admin = implRetroFit.usuarioLogin(loginRequest);

            if(admin.getRol().equals("ADMIN")) {

                secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
                // Mostrar la segunda escena en el stage actual
                currentStage.setScene(secondScene);

                currentStage.show();
            }else{
                lblError.setVisible(true);
                lblError.setText("Usuario sin permisos de Administrador, permisos: "+admin.getRol());
            }
        } catch (IOException | NullPointerException e) {
            lblError.setVisible(true);
            lblError.setText("Usuario no existente o contraseña incorrecta");
        }
    }
}
