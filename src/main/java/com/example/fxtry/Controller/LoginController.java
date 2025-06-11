package com.example.fxtry.Controller;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.example.fxtry.Retrofit.RetrofitClient;

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
    private Hyperlink forgotPasswordLink;

    @FXML
    private Hyperlink serverConfigLink;

    @FXML
    private ProgressIndicator loginProgress;

    @FXML
    private void initialize(){
        implRetroFit = new ImplRetroFit();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        imgPortrait.setImage(image);

        // Initialize progress indicator as invisible
        if (loginProgress != null) {
            loginProgress.setVisible(false);
        }
    }

    @FXML
    private void goToMain(ActionEvent event) {
        // Clear any previous error messages
        lblError.setVisible(false);

        // Show progress indicator
        if (loginProgress != null) {
            loginProgress.setVisible(true);
        }

        // Create login request
        LogginRequest loginRequest = new LogginRequest();
        loginRequest.setUserName(lblName.getText().trim());
        loginRequest.setContraseña(lblPasword.getText());

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Make asynchronous login request
        implRetroFit.usuarioLoginAsync(loginRequest, new ImplRetroFit.LoginCallback() {
            @Override
            public void onSuccess(UsuarioDTO usuario) {
                // This runs on a background thread, so we need to use Platform.runLater
                // to update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    try {
                        // Hide progress indicator
                        if (loginProgress != null) {
                            loginProgress.setVisible(false);
                        }

                        // Store the user in the static admin variable
                        admin = usuario;

                        // Check if user has admin role
                        if (admin.getRol().equals("ADMIN")) {
                            // Load the main view
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
                            Parent secondSceneParent = loader.load();
                            Scene secondScene = new Scene(secondSceneParent);

                            // Apply CSS
                            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

                            // Show the main scene
                            currentStage.setScene(secondScene);
                            currentStage.show();
                        } else {
                            // Show error for non-admin users
                            lblError.setVisible(true);
                            lblError.setText("Usuario sin permisos de Administrador, permisos: " + admin.getRol());
                        }
                    } catch (IOException e) {
                        // Handle any errors loading the main view
                        lblError.setVisible(true);
                        String errorMsg = "Error al cargar la vista principal: " + e.getMessage();
                        lblError.setText(errorMsg);
                        e.printStackTrace(); // Print stack trace for debugging

                        // Log the error to console for debugging
                        System.err.println("[ERROR] Failed to load main view: " + e.getMessage());
                        System.err.println("[ERROR] Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "Unknown"));
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // This runs on a background thread, so we need to use Platform.runLater
                // to update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    // Hide progress indicator
                    if (loginProgress != null) {
                        loginProgress.setVisible(false);
                    }

                    // Show error message
                    lblError.setVisible(true);
                    lblError.setText(errorMessage);
                });
            }
        });
    }

    @FXML
    private void goToResetPassword(ActionEvent event) {
        try {
            // Load the reset password view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/reset-password-view.fxml"));
            Parent resetPasswordParent = loader.load();
            Scene resetPasswordScene = new Scene(resetPasswordParent);

            // Apply CSS
            resetPasswordScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Show the reset password scene
            currentStage.setScene(resetPasswordScene);
            currentStage.show();
        } catch (IOException e) {
            lblError.setVisible(true);
            lblError.setText("Error al cargar la página de restablecimiento de contraseña");
            e.printStackTrace();
        }
    }

    @FXML
    private void showServerConfig(ActionEvent event) {
        // Create a dialog for server configuration
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Configuración del Servidor");
        dialog.setHeaderText("Ingrese la dirección del servidor");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the server address field and add it to the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField serverAddress = new TextField();
        serverAddress.setText(RetrofitClient.getBaseUrl());
        serverAddress.setPromptText("http://localhost:8080/");

        grid.add(new Label("Dirección del servidor:"), 0, 0);
        grid.add(serverAddress, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the server address field by default
        Platform.runLater(serverAddress::requestFocus);

        // Convert the result to a server address when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return serverAddress.getText();
            }
            return null;
        });

        // Show the dialog and process the result
        dialog.showAndWait().ifPresent(serverUrl -> {
            if (!serverUrl.isEmpty()) {
                // Update the server URL in RetrofitClient
                RetrofitClient.setBaseUrl(serverUrl);
                lblError.setVisible(true);
                lblError.setText("Configuración del servidor actualizada");
            }
        });
    }
}
