package com.example.fxtry.Controller.Create;

import com.example.fxtry.Controller.AlertController;
import com.example.fxtry.Controller.PresupuestosController;
import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.PresupuestosDTO;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PresupuestosCreateController {

    private ImplRetroFit implRetroFit;

    @FXML
    private ComboBox<JardinesDTO> cmbJardin;

    @FXML
    private ComboBox<SolicitudDTO> cmbSolicitud;

    @FXML
    private TextField txtUbicacion;

    @FXML
    private CheckBox chkRechazado;

    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Load jardines for the combo box
        try {
            List<JardinesDTO> jardines = implRetroFit.getJardines();
            cmbJardin.getItems().addAll(jardines);
            
            // Set a custom cell factory to display the garden location
            cmbJardin.setCellFactory(param -> new javafx.scene.control.ListCell<JardinesDTO>() {
                @Override
                protected void updateItem(JardinesDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getLocalizacion());
                    }
                }
            });
            
            // Set the same converter for the display text
            cmbJardin.setConverter(new javafx.util.StringConverter<JardinesDTO>() {
                @Override
                public String toString(JardinesDTO jardin) {
                    if (jardin == null) {
                        return null;
                    }
                    return jardin.getLocalizacion();
                }

                @Override
                public JardinesDTO fromString(String string) {
                    return null; // Not needed for this use case
                }
            });
        } catch (IOException e) {
            lblError.setText("Error al cargar jardines: " + e.getMessage());
            e.printStackTrace();
        }

        // Load solicitudes for the combo box
        try {
            List<SolicitudDTO> solicitudes = implRetroFit.getSolicitudes();
            cmbSolicitud.getItems().addAll(solicitudes);
            
            // Set a custom cell factory to display the solicitud information
            cmbSolicitud.setCellFactory(param -> new javafx.scene.control.ListCell<SolicitudDTO>() {
                @Override
                protected void updateItem(SolicitudDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        try {
                            // Get the user associated with the solicitud
                            if (item.getIdUsuario() != null) {
                                setText("Solicitud #" + item.getId());
                            } else {
                                setText("Solicitud #" + item.getId() + " (sin usuario)");
                            }
                        } catch (Exception e) {
                            setText("Solicitud #" + item.getId() + " (error)");
                        }
                    }
                }
            });
            
            // Set the same converter for the display text
            cmbSolicitud.setConverter(new javafx.util.StringConverter<SolicitudDTO>() {
                @Override
                public String toString(SolicitudDTO solicitud) {
                    if (solicitud == null) {
                        return null;
                    }
                    return "Solicitud #" + solicitud.getId();
                }

                @Override
                public SolicitudDTO fromString(String string) {
                    return null; // Not needed for this use case
                }
            });
        } catch (IOException e) {
            lblError.setText("Error al cargar solicitudes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void create(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            PresupuestosDTO presupuestoDTO = new PresupuestosDTO();
            
            // Set the garden ID
            JardinesDTO selectedJardin = cmbJardin.getValue();
            if (selectedJardin != null) {
                presupuestoDTO.setIdJardin(selectedJardin.getId());
            }
            
            // Set the solicitud ID
            SolicitudDTO selectedSolicitud = cmbSolicitud.getValue();
            if (selectedSolicitud != null) {
                presupuestoDTO.setIdSolicitud(selectedSolicitud.getId());
            }
            
            // Set the location
            presupuestoDTO.setUbicacion(txtUbicacion.getText().trim());
            
            // Set the rejected status
            presupuestoDTO.setRechazado(chkRechazado.isSelected());
            
            // Set the current date as the send date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            presupuestoDTO.setFechalEnvio(LocalDateTime.now().format(formatter));

            // Create the presupuesto
            implRetroFit.createPresupuesto(presupuestoDTO);

            AlertController.showInformation("Presupuesto Creado", "El presupuesto ha sido creado correctamente.");
            navigateToPresupuestosView(event);
        } catch (IOException e) {
            lblError.setText("Error al crear el presupuesto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (cmbJardin.getValue() == null) {
            lblError.setText("Por favor, seleccione un jardín.");
            return false;
        }

        if (cmbSolicitud.getValue() == null) {
            lblError.setText("Por favor, seleccione una solicitud.");
            return false;
        }

        if (isEmpty(txtUbicacion.getText())) {
            lblError.setText("Por favor, ingrese una ubicación.");
            return false;
        }

        // Clear any previous error
        lblError.setText("");
        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    @FXML
    private void goToPresupuestos(ActionEvent event) {
        navigateToPresupuestosView(event);
    }

    private void navigateToPresupuestosView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Presupuestos/presupuestos-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            PresupuestosController secondController = loader.getController();

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