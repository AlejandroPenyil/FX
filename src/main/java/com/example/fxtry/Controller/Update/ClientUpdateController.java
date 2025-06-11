package com.example.fxtry.Controller.Update;

import com.example.fxtry.Controller.AlertController;
import com.example.fxtry.Controller.ClientController;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

import static com.example.fxtry.Controller.ClientController.updatable;

public class ClientUpdateController {

    private ImplRetroFit implRetroFit;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9}$");

    @FXML
    private TextField txtName, txtDireccion, txtCorreo, txtTelefono;

    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Load existing data
        if (updatable != null) {
            txtName.setText(updatable.getUserName());
            txtDireccion.setText(updatable.getDireccion() != null ? updatable.getDireccion() : "");
            txtTelefono.setText(updatable.getTelefono() != null ? updatable.getTelefono() : "");
            txtCorreo.setText(updatable.getCorreo() != null ? updatable.getCorreo() : "");
        }

        // Add focus listeners for real-time validation
        txtCorreo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                validateEmail();
            }
        });

        txtTelefono.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                validatePhone();
            }
        });
    }

    @FXML
    private void goToClient(ActionEvent event) {
        navigateToClientView(event);
    }

    @FXML
    private void update(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            // Update the user object
            updatable.setUserName(txtName.getText().trim());
            updatable.setDireccion(txtDireccion.getText().trim());
            updatable.setTelefono(txtTelefono.getText().trim());
            updatable.setCorreo(txtCorreo.getText().trim());

            // Save changes
            implRetroFit.putUsuario(updatable);

            // Show success message
            AlertController.showInformation("Cliente Actualizado", "Los datos del cliente han sido actualizados correctamente.");

            // Navigate back to client view
            navigateToClientView(event);
        } catch (IOException e) {
            lblError.setText("Error al actualizar el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (isEmpty(txtName.getText())) {
            lblError.setText("El nombre de usuario es obligatorio.");
            return false;
        }

        // Validate email format
        if (!validateEmail()) {
            return false;
        }

        // Validate phone format
        if (!isEmpty(txtTelefono.getText()) && !validatePhone()) {
            return false;
        }

        // Clear any previous error
        lblError.setText("");
        return true;
    }

    private boolean validateEmail() {
        String email = txtCorreo.getText().trim();
        if (!isEmpty(email) && !EMAIL_PATTERN.matcher(email).matches()) {
            lblError.setText("Por favor, ingrese un correo electrónico válido.");
            return false;
        }
        return true;
    }

    private boolean validatePhone() {
        String phone = txtTelefono.getText().trim();
        if (!isEmpty(phone) && !PHONE_PATTERN.matcher(phone).matches()) {
            lblError.setText("Por favor, ingrese un número de teléfono válido (9 dígitos).");
            return false;
        }
        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    private void navigateToClientView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            ClientController secondController = loader.getController();

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            lblError.setText("Error al navegar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
