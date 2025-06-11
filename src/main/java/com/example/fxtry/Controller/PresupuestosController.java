package com.example.fxtry.Controller;

import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.PresupuestosDTO;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.fxtry.Controller.AlertController.showAlert;

public class PresupuestosController {
    ImplRetroFit implRetroFit;

    // Static variable to hold the presupuesto being updated
    public static PresupuestosDTO presupuestoToUpdate = null;

    @FXML
    private VBox presupuestosContainer;

    @FXML
    private TextField searchField;

    // Track the currently selected presupuesto
    private PresupuestosDTO selectedPresupuesto = null;

    // Track all presupuesto cards for selection management
    private List<Parent> presupuestoCards = new ArrayList<>();

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Add search functionality if searchField exists
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    applyFilter(newVal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // Load and display presupuestos
        try {
            List<PresupuestosDTO> presupuestosList = implRetroFit.getPresupuestos();
            displayPresupuestos(presupuestosList);
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo obtener la lista de presupuestos");
        }
    }

    // Method to display presupuestos in the card view
    private void displayPresupuestos(List<PresupuestosDTO> presupuestos) {
        // Clear existing presupuestos
        presupuestosContainer.getChildren().clear();

        // Clear tracking list
        presupuestoCards.clear();

        // Reset selected presupuesto
        selectedPresupuesto = null;

        // Add presupuesto cards
        for (PresupuestosDTO presupuesto : presupuestos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Presupuestos/presupuesto-card.fxml"));
                Parent presupuestoCard = loader.load();

                // Get controller and set presupuesto data
                PresupuestoCardController cardController = loader.getController();
                cardController.setParentController(this);
                cardController.setPresupuesto(presupuesto);

                // Add card to container
                presupuestosContainer.getChildren().add(presupuestoCard);

                // Add to tracking list for selection management
                presupuestoCards.add(presupuestoCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to filter presupuestos based on search text
    private void applyFilter(String searchText) throws IOException {
        if (searchText == null || searchText.isEmpty()) {
            // If search is empty, show all presupuestos
            List<PresupuestosDTO> presupuestosList = implRetroFit.getPresupuestos();
            displayPresupuestos(presupuestosList);
            return;
        }

        // Convert search text to lowercase for case-insensitive search
        searchText = searchText.toLowerCase();

        // Get all presupuestos
        List<PresupuestosDTO> allPresupuestos = implRetroFit.getPresupuestos();
        List<PresupuestosDTO> filteredPresupuestos = new ArrayList<>();

        // Filter presupuestos based on search text
        for (PresupuestosDTO presupuesto : allPresupuestos) {
            // Get jardin for this presupuesto
            JardinesDTO jardin = null;
            try {
                jardin = implRetroFit.getJardine(presupuesto.getIdJardin());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jardinName = "Desconocido";
            if (jardin != null) {
                jardinName = jardin.getLocalizacion();
            }

            // Get solicitud for this presupuesto
            SolicitudDTO solicitud = null;
            String clienteName = "Desconocido";
            try {
                solicitud = implRetroFit.getSolicitud(presupuesto.getIdSolicitud());
                if (solicitud != null) {
                    UsuarioDTO cliente = implRetroFit.getUsuario(solicitud.getIdUsuario());
                    if (cliente != null) {
                        clienteName = cliente.getNombre() + " " + cliente.getApellidos();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jardinName.toLowerCase().contains(searchText) ||
                clienteName.toLowerCase().contains(searchText) ||
                presupuesto.getUbicacion().toLowerCase().contains(searchText)) {
                filteredPresupuestos.add(presupuesto);
            }
        }

        // Display filtered presupuestos
        displayPresupuestos(filteredPresupuestos);
    }

    // Method to set the selected presupuesto (will be called from PresupuestoCardController)
    public void setSelectedPresupuesto(PresupuestosDTO presupuesto, Parent card) {
        this.selectedPresupuesto = presupuesto;

        // Clear selection from all cards
        for (Parent presupuestoCard : presupuestoCards) {
            presupuestoCard.getStyleClass().remove("document-card-selected");
        }

        // Add selection to the clicked card
        if (card != null) {
            card.getStyleClass().add("document-card-selected");
        }
    }
    public void goToCreate(ActionEvent event) {
        try {
            // Navigate to the create view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Presupuestos/presupuestos-create-view.fxml"));
            Parent createViewParent = loader.load();
            Scene createViewScene = new Scene(createViewParent);

            // Apply stylesheet
            createViewScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // Get the current stage - use a safer approach that works with both Node and MenuItem
            Stage currentStage = null;

            // Try to get the stage from the event source if it's a Node
            if (event.getSource() instanceof javafx.scene.Node) {
                currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            } 

            // If we couldn't get the stage from the event source (e.g., it's a MenuItem)
            // Get all windows and find the focused one
            if (currentStage == null) {
                for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                    if (window instanceof Stage && window.isFocused()) {
                        currentStage = (Stage) window;
                        break;
                    }
                }
            }

            // If we still couldn't find the stage, use the first available stage
            if (currentStage == null && !javafx.stage.Window.getWindows().isEmpty()) {
                javafx.stage.Window window = javafx.stage.Window.getWindows().get(0);
                if (window instanceof Stage) {
                    currentStage = (Stage) window;
                }
            }

            // Set the scene if we found a stage
            if (currentStage != null) {
                currentStage.setScene(createViewScene);
                currentStage.show();
            } else {
                throw new IllegalStateException("No stage found to display the view");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo abrir la vista de creación: " + e.getMessage());
        }
    }

    public void goToUpdate(ActionEvent event) {
        if (selectedPresupuesto == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un presupuesto primero");
            return;
        }

        try {
            // Set the static variable for the update controller
            presupuestoToUpdate = selectedPresupuesto;

            // Navigate to the update view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Presupuestos/presupuestos-update-view.fxml"));
            Parent updateViewParent = loader.load();
            Scene updateViewScene = new Scene(updateViewParent);

            // Apply stylesheet
            updateViewScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // Get the current stage - use a safer approach that works with both Node and MenuItem
            Stage currentStage = null;

            // Try to get the stage from the event source if it's a Node
            if (event.getSource() instanceof javafx.scene.Node) {
                currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            } 

            // If we couldn't get the stage from the event source (e.g., it's a MenuItem)
            // Get all windows and find the focused one
            if (currentStage == null) {
                for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                    if (window instanceof Stage && window.isFocused()) {
                        currentStage = (Stage) window;
                        break;
                    }
                }
            }

            // If we still couldn't find the stage, use the first available stage
            if (currentStage == null && !javafx.stage.Window.getWindows().isEmpty()) {
                javafx.stage.Window window = javafx.stage.Window.getWindows().get(0);
                if (window instanceof Stage) {
                    currentStage = (Stage) window;
                }
            }

            // Set the scene if we found a stage
            if (currentStage != null) {
                currentStage.setScene(updateViewScene);
                currentStage.show();
            } else {
                throw new IllegalStateException("No stage found to display the view");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo abrir la vista de actualización: " + e.getMessage());
        }
    }

    public void delete(ActionEvent event) {
        if (selectedPresupuesto == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un presupuesto primero");
            return;
        }

        try {
            // TODO: Implementation for deleting the selected presupuesto
            // For now, just show a message
            AlertController.showAlert("Información", "Funcionalidad de eliminación en desarrollo");
        } catch (Exception e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "Error al intentar eliminar el presupuesto: " + e.getMessage());
        }
    }

    @FXML
    public void goToMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            // Apply stylesheet
            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // Get the current stage - use a safer approach that works with both Node and MenuItem
            Stage currentStage = null;

            // Try to get the stage from the event source if it's a Node
            if (event.getSource() instanceof javafx.scene.Node) {
                currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            } 

            // If we couldn't get the stage from the event source (e.g., it's a MenuItem)
            // Get all windows and find the focused one
            if (currentStage == null) {
                for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                    if (window instanceof Stage && window.isFocused()) {
                        currentStage = (Stage) window;
                        break;
                    }
                }
            }

            // If we still couldn't find the stage, use the first available stage
            if (currentStage == null && !javafx.stage.Window.getWindows().isEmpty()) {
                javafx.stage.Window window = javafx.stage.Window.getWindows().get(0);
                if (window instanceof Stage) {
                    currentStage = (Stage) window;
                }
            }

            // Set the scene if we found a stage
            if (currentStage != null) {
                currentStage.setScene(secondScene);
                currentStage.show();
            } else {
                throw new IllegalStateException("No stage found to display the view");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo abrir la vista principal: " + e.getMessage());
        }
    }
}
