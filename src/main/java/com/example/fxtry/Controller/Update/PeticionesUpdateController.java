package com.example.fxtry.Controller.Update;

import com.example.fxtry.Controller.AlertController;
import com.example.fxtry.Controller.PeticionesController;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PeticionesUpdateController {

    private ImplRetroFit implRetroFit;
    private SolicitudDTO solicitudToUpdate;

    @FXML
    private TextField txtIdUsuario;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private CheckBox chkAtendida;

    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Load the solicitud to update from the static variable in PeticionesController
        solicitudToUpdate = PeticionesController.updatable;

        if (solicitudToUpdate != null && solicitudToUpdate.getId() != null) {
            // Populate the form with the solicitud data
            txtIdUsuario.setText(String.valueOf(solicitudToUpdate.getIdUsuario()));
            txtDescripcion.setText(solicitudToUpdate.getDescripcion());
            chkAtendida.setSelected(solicitudToUpdate.isAtendida());
        } else {
            lblError.setText("Error: No se ha seleccionado ninguna solicitud para actualizar.");
        }
    }

    @FXML
    private void goToPeticiones(ActionEvent event) {
        navigateToPeticionesView(event);
    }

    @FXML
    private void update(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            if (solicitudToUpdate == null || solicitudToUpdate.getId() == null) {
                lblError.setText("Error: No se ha seleccionado ninguna solicitud para actualizar.");
                return;
            }

            // Update the solicitud with the form data
            solicitudToUpdate.setIdUsuario(Integer.parseInt(txtIdUsuario.getText().trim()));
            solicitudToUpdate.setDescripcion(txtDescripcion.getText().trim());
            solicitudToUpdate.setAtendida(chkAtendida.isSelected());

            implRetroFit.updateSolicitud(solicitudToUpdate.getId(), solicitudToUpdate);

            AlertController.showInformation("Solicitud Actualizada", "La solicitud ha sido actualizada correctamente.");
            navigateToPeticionesView(event);
        } catch (NumberFormatException e) {
            lblError.setText("Error: El ID de usuario debe ser un número entero.");
            e.printStackTrace();
        } catch (IOException e) {
            lblError.setText("Error al actualizar la solicitud: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (isEmpty(txtIdUsuario.getText()) || isEmpty(txtDescripcion.getText())) {
            lblError.setText("Por favor, complete todos los campos obligatorios.");
            return false;
        }

        // Validate that idUsuario is a number
        try {
            Integer.parseInt(txtIdUsuario.getText().trim());
        } catch (NumberFormatException e) {
            lblError.setText("El ID de usuario debe ser un número entero.");
            return false;
        }

        // Clear any previous error
        lblError.setText("");
        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    private void navigateToPeticionesView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Peticiones/peticiones-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            PeticionesController secondController = loader.getController();

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
