package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Create.ImagenesCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
import com.example.fxtry.Controller.Update.ImagenesUpdateController;
import com.example.fxtry.Model.ImagenDTO;
import com.example.fxtry.Model.JardinesDTO;
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

import static com.example.fxtry.Controller.AlertController.showAlert;

public class ImagenesController {
    public static ImagenDTO updatable = new ImagenDTO();
    ImplRetroFit implRetroFit;

    @FXML
    private VBox imagenesContainer;

    @FXML
    private TextField searchField;

    // Track the currently selected imagen
    private ImagenDTO selectedImagen = null;

    // Track all imagen cards for selection management
    private List<Parent> imagenCards = new ArrayList<>();

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

        // Load and display imagenes
        try {
            List<ImagenDTO> imagenesList = implRetroFit.getImagenes();
            displayImagenes(imagenesList);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo obtener la lista de imágenes");
        }
    }

    // Method to display imagenes in the card view
    private void displayImagenes(List<ImagenDTO> imagenes) {
        // Clear existing imagenes
        imagenesContainer.getChildren().clear();

        // Clear tracking list
        imagenCards.clear();

        // Reset selected imagen
        selectedImagen = null;

        // Add imagen cards
        for (ImagenDTO imagen : imagenes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Imagenes/imagen-card.fxml"));
                Parent imagenCard = loader.load();

                // Get controller and set imagen data
                ImagenCardController cardController = loader.getController();
                cardController.setParentController(this);
                cardController.setImagen(imagen);

                // Add card to container
                imagenesContainer.getChildren().add(imagenCard);

                // Add to tracking list for selection management
                imagenCards.add(imagenCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to filter imagenes based on search text
    private void applyFilter(String searchText) throws IOException {
        if (searchText == null || searchText.isEmpty()) {
            // If search is empty, show all imagenes
            List<ImagenDTO> imagenesList = implRetroFit.getImagenes();
            displayImagenes(imagenesList);
            return;
        }

        // Convert search text to lowercase for case-insensitive search
        searchText = searchText.toLowerCase();

        // Get all imagenes
        List<ImagenDTO> allImagenes = implRetroFit.getImagenes();
        List<ImagenDTO> filteredImagenes = new ArrayList<>();

        // Filter imagenes based on search text
        for (ImagenDTO imagen : allImagenes) {
            // Get client name for this imagen
            UsuarioDTO cliente = null;
            try {
                cliente = implRetroFit.getUsuario(imagen.getIdUsuario());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String clienteName = "Desconocido";
            if (cliente != null) {
                clienteName = cliente.getNombre() + " " + cliente.getApellidos();
            }

            // Get jardin name for this imagen
            JardinesDTO jardin = null;
            try {
                jardin = implRetroFit.getJardine(imagen.getIdJardin());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jardinName = "Desconocido";
            if (jardin != null) {
                jardinName = jardin.getLocalizacion();
            }

            if (clienteName.toLowerCase().contains(searchText) ||
                jardinName.toLowerCase().contains(searchText) ||
                imagen.getFecha().toLowerCase().contains(searchText) ||
                imagen.getUbicacion().toLowerCase().contains(searchText) ||
                imagen.getComentario().toLowerCase().contains(searchText)) {
                filteredImagenes.add(imagen);
            }
        }

        // Display filtered imagenes
        displayImagenes(filteredImagenes);
    }

    // Method to set the selected imagen (will be called from ImagenCardController)
    public void setSelectedImagen(ImagenDTO imagen, Parent card) {
        this.selectedImagen = imagen;

        // Clear selection from all cards
        for (Parent imagenCard : imagenCards) {
            imagenCard.getStyleClass().remove("document-card-selected");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Imagenes/imagenes-create-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ImagenesCreateController secondController = loader.getController();

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
            if (selectedImagen == null) {
                showAlert("Advertencia", "Por favor, seleccione una imagen primero");
                return;
            }

            updatable = selectedImagen;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Imagenes/imagenes-update-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ImagenesUpdateController secondController = loader.getController();

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
        if (selectedImagen == null) {
            showAlert("Advertencia", "Por favor, seleccione una imagen primero");
            return;
        }

        //TODO metodo para softdelete, no va haber hard delete
    }
}
