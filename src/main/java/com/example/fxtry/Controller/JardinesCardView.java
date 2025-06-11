package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.JardinCreateController;
import com.example.fxtry.Model.*;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * This is a card-based version of the JardinesController
 * It replaces the table view with a card-based view for better visibility
 */
public class JardinesCardView {
    ImplRetroFit implRetroFit;

    @FXML
    private VBox jardinesContainer;

    @FXML
    private TextField searchField;

    @FXML
    private TextField txtComentario;

    // Track the currently selected garden
    private JardinesDTO selectedJardin = null;

    // Track all garden cards for selection management
    private List<Parent> jardinCards = new ArrayList<>();

    @FXML
    private void initialize() throws IOException {
        implRetroFit = new ImplRetroFit();

        // Add search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                applyFilter(newVal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Load and display gardens
        try {
            List<JardinesDTO> jardinDTOList = implRetroFit.getJardines();
            displayJardines(jardinDTOList);
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo obtener la lista de jardines");
        }
    }

    // Method to display gardens in the card view
    private void displayJardines(List<JardinesDTO> jardines) {
        // Clear existing gardens
        jardinesContainer.getChildren().clear();

        // Clear tracking list
        jardinCards.clear();

        // Reset selected garden
        selectedJardin = null;

        // Add garden cards
        for (JardinesDTO jardin : jardines) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/jardin/jardin-card.fxml"));
                Parent jardinCard = loader.load();

                // Get controller and set garden data
                JardinCardController cardController = loader.getController();
                cardController.setParentController(this);
                cardController.setJardin(jardin);

                // Add card to container
                jardinesContainer.getChildren().add(jardinCard);

                // Add to tracking list for selection management
                jardinCards.add(jardinCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to filter gardens based on search text
    private void applyFilter(String searchText) throws IOException {
        if (searchText == null || searchText.isEmpty()) {
            // If search is empty, show all gardens
            List<JardinesDTO> jardinDTOList = implRetroFit.getJardines();
            displayJardines(jardinDTOList);
            return;
        }

        // Convert search text to lowercase for case-insensitive search
        searchText = searchText.toLowerCase();

        // Get all gardens
        List<JardinesDTO> allJardines = implRetroFit.getJardines();
        List<JardinesDTO> filteredJardines = new ArrayList<>();

        // Filter gardens based on search text
        for (JardinesDTO jardin : allJardines) {
            if (jardin.getLocalizacion().toLowerCase().contains(searchText)) {
                filteredJardines.add(jardin);
            }
        }

        // Display filtered gardens
        displayJardines(filteredJardines);
    }

    // Method to set the selected garden (will be called from JardinCardController)
    public void setSelectedJardin(JardinesDTO jardin, Parent card) {
        this.selectedJardin = jardin;

        // Clear selection from all cards
        for (Parent jardinCard : jardinCards) {
            jardinCard.getStyleClass().remove("document-card-selected");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/jardin/jardin-create-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            JardinCreateController secondController = loader.getController();

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
        if (selectedJardin == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un jardín primero");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/jardin/update-update-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            // Get the controller and set the garden to update
            com.example.fxtry.Controller.Update.JardinesUpdateController updateController = loader.getController();
            updateController.setJardinToUpdate(selectedJardin);

            // Get the current stage
            MenuItem menuItem = (MenuItem) event.getSource();
            ContextMenu contextMenu = menuItem.getParentPopup();
            Scene scene = contextMenu.getOwnerNode().getScene();
            Stage currentStage = (Stage) scene.getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo abrir la ventana de actualización: " + e.getMessage());
        }
    }

    public void subir(ActionEvent event) {
        if (selectedJardin == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un jardín primero");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Busca la imagen");

        // Agregar filtros de extensión si es necesario
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images Files", "*.png", ".jpg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        ImagenDTO imagenDTO = new ImagenDTO();

        // Mostrar el FileChooser y capturar el archivo seleccionado
        File selectedFile = fileChooser.showOpenDialog(currentStage);
        if (selectedFile != null) {
            try {
                ImagenUploadDto imagenUploadDto = new ImagenUploadDto();
                // Leer el contenido del archivo en un arreglo de bytes
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());

                imagenDTO.setIdJardin(selectedJardin.getId());
                imagenDTO.setIdUsuario(selectedJardin.getIdCliente());
                imagenDTO.setComentario(txtComentario.getText());
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                imagenUploadDto.setContent(encodedString);
                imagenUploadDto.setFileName(selectedFile.getName());
                imagenUploadDto.setFileType(Files.probeContentType(selectedFile.toPath()));
                imagenUploadDto.setImagenDTO(imagenDTO);

                System.out.println(imagenUploadDto.getFileName());

                implRetroFit.uploadImagenes(imagenUploadDto);

                // Aquí puedes guardar la cadena codificada en Base64 en algún lugar si lo necesitas
            } catch (IOException e) {
                e.printStackTrace();
                AlertController.showAlert("File Error", "Could not read the file: " + e.getMessage());
            }
        }
    }

    public void subirPresupuesto(ActionEvent event) {
        if (selectedJardin == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un jardín primero");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Busca el presupuesto");

        // Agregar filtros de extensión si es necesario
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Mostrar el FileChooser y capturar el archivo seleccionado
        File selectedFile = fileChooser.showOpenDialog(currentStage);
        if (selectedFile != null) {
            try {
                FileUpload imagenUploadDto = new FileUpload();
                // Leer el contenido del archivo en un arreglo de bytes
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());

                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                imagenUploadDto.setContent(encodedString);
                imagenUploadDto.setFileName(selectedFile.getName());
                imagenUploadDto.setFileType(Files.probeContentType(selectedFile.toPath()));
                imagenUploadDto.setId(selectedJardin.getId());

                System.out.println(imagenUploadDto.getId());

                implRetroFit.uploadPresupuesto(imagenUploadDto);

                // Aquí puedes guardar la cadena codificada en Base64 en algún lugar si lo necesitas
            } catch (IOException e) {
                e.printStackTrace();
                AlertController.showAlert("File Error", "Could not read the file: " + e.getMessage());
            }
        }
    }
}
