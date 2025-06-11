package com.example.fxtry.Controller;

import com.example.fxtry.Model.PasswordResetRequest;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ResetPasswordController {

    private ImplRetroFit implRetroFit;

    @FXML
    private ImageView imgPortrait;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label lblError;

    @FXML
    private Hyperlink backToLoginLink;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        imgPortrait.setImage(image);

        // Clear any error messages
        lblError.setVisible(false);
    }

    @FXML
    private void resetPassword(ActionEvent event) {
        // Clear previous error messages
        lblError.setVisible(false);

        // Get the input values
        String email = emailField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate inputs
        if (email.isEmpty()) {
            showError("Por favor, ingrese su correo electrónico");
            return;
        }

        if (newPassword.isEmpty()) {
            showError("Por favor, ingrese su nueva contraseña");
            return;
        }

        if (confirmPassword.isEmpty()) {
            showError("Por favor, confirme su nueva contraseña");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden");
            return;
        }

        // Create a PasswordResetRequest object
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setIdentifier(email);
        resetRequest.setNewPassword(newPassword);

        try {
            // Call the API to reset the password
            boolean success = implRetroFit.resetPassword(resetRequest);

            if (success) {
                // Show success message
                showError("Contraseña restablecida con éxito. Por favor, inicie sesión con su nueva contraseña.");
                lblError.setStyle("-fx-text-fill: green;");

                // Automatically go back to login after a short delay
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(() -> {
                            try {
                                goBackToLogin(event);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                // Show error message
                showError("Error al restablecer la contraseña. Por favor, inténtelo de nuevo.");
                lblError.setStyle("-fx-text-fill: red;");
            }
        } catch (IOException e) {
            // Show error message
            showError("Error de conexión. Por favor, inténtelo de nuevo más tarde.");
            lblError.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToLogin(ActionEvent event) {
        try {
            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/login-view.fxml"));
            Parent loginParent = loader.load();
            Scene loginScene = new Scene(loginParent);

            // Apply CSS
            loginScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Show the login scene
            currentStage.setScene(loginScene);
            currentStage.show();
        } catch (IOException e) {
            showError("Error al cargar la página de inicio de sesión");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}
