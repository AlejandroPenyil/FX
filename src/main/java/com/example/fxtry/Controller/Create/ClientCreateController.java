package com.example.fxtry.Controller.Create;

import com.example.fxtry.Controller.AlertController;
import com.example.fxtry.Controller.ClientController;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class ClientCreateController {

    private ImplRetroFit implRetroFit;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}[A-Za-z]$");

    @FXML
    private TextField txtNombre, txtDireccion, txtCorreo, txtTelefono, txtApellidos, txtDni, txtUsuario;

    @FXML
    private PasswordField txtContraseña;

    @FXML
    private ComboBox<String> cmbRol;

    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        cmbRol.getItems().addAll("ADMIN", "CLIENT", "GUEST");
        cmbRol.getSelectionModel().selectFirst();

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

        txtDni.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                validateDNI();
            }
        });
    }

    @FXML
    private void goToClient(ActionEvent event) {
        navigateToClientView(event);
    }

    @FXML
    private void create(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNombre(txtNombre.getText().trim());
            usuarioDTO.setApellidos(txtApellidos.getText().trim());
            usuarioDTO.setCorreo(txtCorreo.getText().trim());
            usuarioDTO.setTelefono(txtTelefono.getText().trim());
            usuarioDTO.setDni(txtDni.getText().trim());
            usuarioDTO.setContraseña(txtContraseña.getText());
            usuarioDTO.setRol(cmbRol.getValue());
            usuarioDTO.setUserName(txtUsuario.getText().trim());
            usuarioDTO.setDireccion(txtDireccion.getText().trim());

            implRetroFit.usarioCreate(usuarioDTO);

            AlertController.showInformation("Cliente Creado", "El cliente ha sido creado correctamente.");
            navigateToClientView(event);
        } catch (IOException e) {
            lblError.setText("Error al crear el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (isEmpty(txtNombre.getText()) || isEmpty(txtApellidos.getText()) || 
            isEmpty(txtUsuario.getText()) || isEmpty(txtCorreo.getText()) ||
            isEmpty(txtContraseña.getText()) || isEmpty(txtDni.getText())) {

            lblError.setText("Por favor, complete todos los campos obligatorios.");
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

        // Validate DNI format
        if (!validateDNI()) {
            return false;
        }

        // Password strength
        if (txtContraseña.getText().length() < 6) {
            lblError.setText("La contraseña debe tener al menos 6 caracteres.");
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

    private boolean validateDNI() {
        String dni = txtDni.getText().trim();
        if (!isEmpty(dni) && !DNI_PATTERN.matcher(dni).matches()) {
            lblError.setText("Por favor, ingrese un DNI válido (8 dígitos seguidos de una letra).");
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
