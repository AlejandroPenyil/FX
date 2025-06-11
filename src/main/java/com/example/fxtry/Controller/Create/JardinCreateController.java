package com.example.fxtry.Controller.Create;

import com.example.fxtry.Controller.AlertController;
import com.example.fxtry.Controller.JardinesCardView;
import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class JardinCreateController {

    private ImplRetroFit implRetroFit;

    @FXML
    private TextField txtNombre, txtUbicacion, txtSuperficie, txtPresupuesto;

    @FXML
    private ComboBox<String> cmbPropietario, cmbTipo, cmbEstado;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private DatePicker dpFechaCreacion;

    @FXML
    private Label lblError;

    private List<UsuarioDTO> usuarios;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Set default date to today
        dpFechaCreacion.setValue(LocalDate.now());

        // Initialize combo boxes
        cmbTipo.getItems().addAll("Residencial", "Comercial", "Público", "Otro");
        cmbEstado.getItems().addAll("Activo", "En Mantenimiento", "Inactivo");

        // Load users for the owner combo box
        loadUsers();

        // Add listeners for numeric fields
        txtSuperficie.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtSuperficie.setText(oldValue);
            }
        });

        txtPresupuesto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtPresupuesto.setText(oldValue);
            }
        });
    }

    private void loadUsers() {
        try {
            usuarios = implRetroFit.getUsuarios();
            for (UsuarioDTO usuario : usuarios) {
                cmbPropietario.getItems().add(usuario.getNombre() + " " + usuario.getApellidos());
            }
        } catch (IOException e) {
            lblError.setText("Error al cargar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void create() {
        if (!validateForm()) {
            return;
        }

        try {
            JardinesDTO jardinDTO = new JardinesDTO();

            // Set location (using the name field as location)
            if (isEmpty(txtNombre.getText())) {
                jardinDTO.setLocalizacion(txtUbicacion.getText().trim());
            } else {
                jardinDTO.setLocalizacion(txtNombre.getText().trim());
            }

            // Set size (using the surface area field as size)
            if (!isEmpty(txtSuperficie.getText())) {
                jardinDTO.setTamaño(Integer.parseInt(txtSuperficie.getText().trim()));
            }

            // Set client ID (using the owner combo box)
            int selectedIndex = cmbPropietario.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < usuarios.size()) {
                jardinDTO.setIdCliente(usuarios.get(selectedIndex).getId());
            }

            // Save the garden
            implRetroFit.createJardin(jardinDTO);

            // Show success message
            AlertController.showInformation("Jardín Creado", "El jardín ha sido creado correctamente.");

            // Navigate back to gardens view
            goToJardines(null);
        } catch (Exception e) {
            lblError.setText("Error al crear el jardín: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (cmbPropietario.getSelectionModel().getSelectedIndex() < 0) {
            lblError.setText("Debe seleccionar un propietario.");
            return false;
        }

        if (!isEmpty(txtSuperficie.getText())) {
            try {
                Integer.parseInt(txtSuperficie.getText().trim());
            } catch (NumberFormatException e) {
                lblError.setText("La superficie debe ser un número entero.");
                return false;
            }
        }

        // Clear any previous error
        lblError.setText("");
        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    @FXML
    private void goToClient(ActionEvent event) {
        goToJardines(event);
    }

    private void goToJardines(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/jardin/jardines-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            JardinesCardView secondController = loader.getController();

            // Get the current stage
            Stage currentStage;
            if (event != null) {
                currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            } else {
                currentStage = (Stage) txtNombre.getScene().getWindow();
            }

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            lblError.setText("Error al navegar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
