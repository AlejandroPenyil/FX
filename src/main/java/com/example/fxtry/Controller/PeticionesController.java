package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Create.PeticionesCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
import com.example.fxtry.Controller.Update.PeticionesUpdateController;
import com.example.fxtry.Model.JardinesDTO;
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

public class PeticionesController {
    public static SolicitudDTO updatable = new SolicitudDTO();
    ImplRetroFit implRetroFit;

    @FXML
    private VBox peticionesContainer;

    @FXML
    private TextField searchField;

    // Track the currently selected peticion
    private SolicitudDTO selectedPeticion = null;

    // Track all peticion cards for selection management
    private List<Parent> peticionCards = new ArrayList<>();

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

        // Load and display peticiones
        try {
            List<SolicitudDTO> peticionesList = implRetroFit.getSolicitudes();
            displayPeticiones(peticionesList);
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo obtener la lista de peticiones");
        }
    }

    // Method to display peticiones in the card view
    private void displayPeticiones(List<SolicitudDTO> peticiones) {
        // Clear existing peticiones
        peticionesContainer.getChildren().clear();

        // Clear tracking list
        peticionCards.clear();

        // Reset selected peticion
        selectedPeticion = null;

        // Add peticion cards
        for (SolicitudDTO peticion : peticiones) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Peticiones/peticion-card.fxml"));
                Parent peticionCard = loader.load();

                // Get controller and set peticion data
                PeticionCardController cardController = loader.getController();
                cardController.setParentController(this);
                cardController.setPeticion(peticion);

                // Add card to container
                peticionesContainer.getChildren().add(peticionCard);

                // Add to tracking list for selection management
                peticionCards.add(peticionCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to filter peticiones based on search text
    private void applyFilter(String searchText) throws IOException {
        if (searchText == null || searchText.isEmpty()) {
            // If search is empty, show all peticiones
            List<SolicitudDTO> peticionesList = implRetroFit.getSolicitudes();
            displayPeticiones(peticionesList);
            return;
        }

        // Convert search text to lowercase for case-insensitive search
        searchText = searchText.toLowerCase();

        // Get all peticiones
        List<SolicitudDTO> allPeticiones = implRetroFit.getSolicitudes();
        List<SolicitudDTO> filteredPeticiones = new ArrayList<>();

        // Filter peticiones based on search text
        for (SolicitudDTO peticion : allPeticiones) {
            // Get client name for this peticion
            UsuarioDTO cliente = null;
            try {
                cliente = implRetroFit.getUsuario(peticion.getIdUsuario());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String clienteName = "Desconocido";
            if (cliente != null) {
                clienteName = cliente.getNombre() + " " + cliente.getApellidos();
            }

            if (clienteName.toLowerCase().contains(searchText) ||
                peticion.getFechaSolicitud().toString().toLowerCase().contains(searchText) ||
                peticion.getDescripcion().toLowerCase().contains(searchText)) {
                filteredPeticiones.add(peticion);
            }
        }

        // Display filtered peticiones
        displayPeticiones(filteredPeticiones);
    }

    // Method to set the selected peticion (will be called from PeticionCardController)
    public void setSelectedPeticion(SolicitudDTO peticion, Parent card) {
        this.selectedPeticion = peticion;

        // Clear selection from all cards
        for (Parent peticionCard : peticionCards) {
            peticionCard.getStyleClass().remove("document-card-selected");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Peticiones/peticiones-create-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            PeticionesCreateController secondController = loader.getController();

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

    public void goToUpdate(ActionEvent event) {
        try {
            if (selectedPeticion == null) {
                AlertController.showAlert("Advertencia", "Por favor, seleccione una petición primero");
                return;
            }

            updatable = selectedPeticion;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Peticiones/peticiones-update-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            PeticionesUpdateController secondController = loader.getController();

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
        if (selectedPeticion == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione una petición primero");
            return;
        }

        //TODO metodo para softdelete, no va haber hard delete
    }
}
