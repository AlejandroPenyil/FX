package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
import com.example.fxtry.Model.FacturaDTO;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacturasController {
    public static FacturaDTO updatable = new FacturaDTO();
    ImplRetroFit implRetroFit;

    @FXML
    private VBox facturasContainer;

    @FXML
    private TextField searchField;

    // Track the currently selected factura
    private FacturaDTO selectedFactura = null;

    // Track all factura cards for selection management
    private List<Parent> facturaCards = new ArrayList<>();

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

        // Load and display facturas
        try {
            List<FacturaDTO> facturasList = implRetroFit.getFacturas();
            displayFacturas(facturasList);
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo obtener la lista de facturas");
        }
    }

    // Method to display facturas in the card view
    private void displayFacturas(List<FacturaDTO> facturas) {
        // Clear existing facturas
        facturasContainer.getChildren().clear();

        // Clear tracking list
        facturaCards.clear();

        // Reset selected factura
        selectedFactura = null;

        // Add factura cards
        for (FacturaDTO factura : facturas) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Facturas/factura-card.fxml"));
                Parent facturaCard = loader.load();

                // Get controller and set factura data
                FacturaCardController cardController = loader.getController();
                cardController.setParentController(this);
                cardController.setFactura(factura);

                // Add card to container
                facturasContainer.getChildren().add(facturaCard);

                // Add to tracking list for selection management
                facturaCards.add(facturaCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to filter facturas based on search text
    private void applyFilter(String searchText) throws IOException {
        if (searchText == null || searchText.isEmpty()) {
            // If search is empty, show all facturas
            List<FacturaDTO> facturasList = implRetroFit.getFacturas();
            displayFacturas(facturasList);
            return;
        }

        // Convert search text to lowercase for case-insensitive search
        searchText = searchText.toLowerCase();

        // Get all facturas
        List<FacturaDTO> allFacturas = implRetroFit.getFacturas();
        List<FacturaDTO> filteredFacturas = new ArrayList<>();

        // Filter facturas based on search text
        for (FacturaDTO factura : allFacturas) {
            // Get client name for this factura
            UsuarioDTO cliente = null;
            try {
                cliente = implRetroFit.getUsuario(factura.getIdCliente());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String clienteName = "Desconocido";
            if (cliente != null) {
                clienteName = cliente.getNombre() + " " + cliente.getApellidos();
            }

            if (clienteName.toLowerCase().contains(searchText) ||
                factura.getFecha().toString().toLowerCase().contains(searchText)) {
                filteredFacturas.add(factura);
            }
        }

        // Display filtered facturas
        displayFacturas(filteredFacturas);
    }

    // Method to set the selected factura (will be called from FacturaCardController)
    public void setSelectedFactura(FacturaDTO factura, Parent card) {
        this.selectedFactura = factura;

        // Clear selection from all cards
        for (Parent facturaCard : facturaCards) {
            facturaCard.getStyleClass().remove("document-card-selected");
        }

        // Add selection to the clicked card
        if (card != null) {
            card.getStyleClass().add("document-card-selected");
        }
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
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCreate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Facturas/facturas-create-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

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

            // Apply stylesheet
            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // Set the scene if we found a stage
            if (currentStage != null) {
                currentStage.setScene(secondScene);
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
        try {
            if (selectedFactura == null) {
                AlertController.showAlert("Advertencia", "Por favor, seleccione una factura primero");
                return;
            }

            updatable = selectedFactura;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-update-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ClientUpdateController secondController = loader.getController();

            // Acceso al stage actual
            MenuItem menuItem = (MenuItem) event.getSource();
            ContextMenu contextMenu = menuItem.getParentPopup();
            Scene scene = contextMenu.getOwnerNode().getScene();
            Stage currentStage = (Stage) scene.getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent event){
        if (selectedFactura == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione una factura primero");
            return;
        }

        //TODO metodo para softdelete, no va haber hard delete
    }
}
