package com.example.fxtry.Controller.Update;

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
import java.util.List;

public class JardinesUpdateController {

    private ImplRetroFit implRetroFit;

    // Garden to be updated
    private JardinesDTO jardinToUpdate;

    @FXML
    private TextField txtNombre, txtUbicacion, txtSuperficie, txtPresupuesto;

    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Label lblError;

    private List<UsuarioDTO> usuarios;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Initialize combo boxes
        cmbEstado.getItems().addAll("Activo", "En Mantenimiento", "Inactivo");

        // Add listeners for numeric fields
        txtSuperficie.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtSuperficie.setText(oldValue);
            }
        });
    }

    // Method to set the garden to be updated
    public void setJardinToUpdate(JardinesDTO jardin) {
        this.jardinToUpdate = jardin;

        // Load existing data
        if (jardinToUpdate != null) {
            txtUbicacion.setText(jardinToUpdate.getLocalizacion() != null ? jardinToUpdate.getLocalizacion() : "");

            if (jardinToUpdate.getTamaño() != null) {
                txtSuperficie.setText(jardinToUpdate.getTamaño().toString());
            }
        }
    }

    @FXML
    private void update() {
        if (!validateForm()) {
            return;
        }

        if (jardinToUpdate == null) {
            lblError.setText("No hay jardín seleccionado para actualizar.");
            return;
        }

        try {
            // Update the garden object
            jardinToUpdate.setLocalizacion(txtUbicacion.getText().trim());

            if (!isEmpty(txtSuperficie.getText())) {
                jardinToUpdate.setTamaño(Integer.parseInt(txtSuperficie.getText().trim()));
            }

            // Save changes
            // implRetroFit.updateJardin(jardinToUpdate); // This method needs to be implemented

            // Show success message
            AlertController.showInformation("Jardín Actualizado", "Los datos del jardín han sido actualizados correctamente.");

            // Navigate back to gardens view
            goToJardines(null);
        } catch (Exception e) {
            lblError.setText("Error al actualizar el jardín: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (isEmpty(txtUbicacion.getText())) {
            lblError.setText("La ubicación del jardín es obligatoria.");
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
                currentStage = (Stage) txtUbicacion.getScene().getWindow();
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
