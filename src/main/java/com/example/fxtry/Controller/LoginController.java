package com.example.fxtry.Controller;

import com.example.fxtry.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.cell.ImageGridCell;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    public static User admin = new User();

    @FXML
    private ImageView imgPortrait;

    @FXML
    private TextField lblName;

    @FXML
    private PasswordField lblPasword;

    @FXML
    private void initialize(){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        imgPortrait.setImage(image);
    }

    @FXML
    private void goToMain(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            // Acceso al controlador de la segunda escena, si es necesario
            MainController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            admin.setNombre(lblName.getText().trim());
            admin.setContraseña(lblPasword.getText());

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
