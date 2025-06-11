package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
import com.example.fxtry.Model.*;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ClientController extends Application {
    public static UsuarioDTO updatable = new UsuarioDTO();

    ImplRetroFit implRetroFit;

    @FXML
    private VBox clientsContainer;

    @FXML
    private TextField searchField;

    // Track the currently selected user
    private UsuarioDTO selectedUsuario = null;

    // Track all client cards for selection management
    private List<Parent> clientCards = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        // Load and display clients
        List<UsuarioDTO> usuarioDTOList = implRetroFit.getUsuarios();
        displayClients(usuarioDTOList);
    }

    @FXML
    private void goToMain(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Store current window dimensions and state
            double width = currentStage.getWidth();
            double height = currentStage.getHeight();
            boolean maximized = currentStage.isMaximized();

            // Calculate the aspect ratio
            double aspectRatio = width / height;

            // If maximized, temporarily set to non-maximized to capture actual size
            if (maximized) {
                currentStage.setMaximized(false);
                // Get the actual window size before maximizing
                width = currentStage.getWidth();
                height = currentStage.getHeight();
                // Recalculate aspect ratio with actual size
                aspectRatio = width / height;
                // Restore maximized state
                currentStage.setMaximized(true);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Parent secondSceneParent = loader.load();

            // Set the preferred size on the root to maintain aspect ratio if it's a Region
            if (secondSceneParent instanceof Region) {
                Region rootRegion = (Region) secondSceneParent;
                rootRegion.setPrefWidth(width);
                rootRegion.setPrefHeight(height);
            }

            Scene secondScene = new Scene(secondSceneParent, width, height);

            // Acceso al controlador de la segunda escena, si es necesario
            MainController secondController = loader.getController();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // First set the scene with the correct dimensions
            currentStage.setScene(secondScene);

            // Then restore maximized state if needed
            if (maximized) {
                currentStage.setMaximized(true);
            } else {
                // Ensure correct dimensions for non-maximized state
                currentStage.setWidth(width);
                currentStage.setHeight(height);
            }

            // Create a final copy of the aspect ratio for use in lambda expressions
            final double finalAspectRatio = aspectRatio;

            // Add a listener to maintain aspect ratio when the window is resized
            currentStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                if (!currentStage.isMaximized()) {
                    currentStage.setHeight(newVal.doubleValue() / finalAspectRatio);
                }
            });

            currentStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                if (!currentStage.isMaximized()) {
                    currentStage.setWidth(newVal.doubleValue() * finalAspectRatio);
                }
            });

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCreate(ActionEvent event) {
        try {
            // Acceso al stage actual
            MenuItem menuItem = (MenuItem) event.getSource();
            ContextMenu contextMenu = menuItem.getParentPopup();
            Scene scene = contextMenu.getOwnerNode().getScene();
            Stage currentStage = (Stage) scene.getWindow();

            // Store current window dimensions and state
            double width = currentStage.getWidth();
            double height = currentStage.getHeight();
            boolean maximized = currentStage.isMaximized();

            // Calculate the aspect ratio
            double aspectRatio = width / height;

            // If maximized, temporarily set to non-maximized to capture actual size
            if (maximized) {
                currentStage.setMaximized(false);
                // Get the actual window size before maximizing
                width = currentStage.getWidth();
                height = currentStage.getHeight();
                // Recalculate aspect ratio with actual size
                aspectRatio = width / height;
                // Restore maximized state
                currentStage.setMaximized(true);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-create-view.fxml"));
            Parent secondSceneParent = loader.load();

            // Set the preferred size on the root to maintain aspect ratio if it's a Region
            if (secondSceneParent instanceof Region) {
                Region rootRegion = (Region) secondSceneParent;
                rootRegion.setPrefWidth(width);
                rootRegion.setPrefHeight(height);
            }

            Scene secondScene = new Scene(secondSceneParent, width, height);

            // Acceso al controlador de la segunda escena, si es necesario
            ClientCreateController secondController = loader.getController();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // First set the scene with the correct dimensions
            currentStage.setScene(secondScene);

            // Then restore maximized state if needed
            if (maximized) {
                currentStage.setMaximized(true);
            } else {
                // Ensure correct dimensions for non-maximized state
                currentStage.setWidth(width);
                currentStage.setHeight(height);
            }

            // Create a final copy of the aspect ratio for use in lambda expressions
            final double finalAspectRatio = aspectRatio;

            // Add a listener to maintain aspect ratio when the window is resized
            currentStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                if (!currentStage.isMaximized()) {
                    currentStage.setHeight(newVal.doubleValue() / finalAspectRatio);
                }
            });

            currentStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                if (!currentStage.isMaximized()) {
                    currentStage.setWidth(newVal.doubleValue() * finalAspectRatio);
                }
            });

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to display clients in the card view
    private void displayClients(List<UsuarioDTO> clients) {
        // Clear existing clients
        clientsContainer.getChildren().clear();

        // Clear tracking list
        clientCards.clear();

        // Reset selected client
        selectedUsuario = null;

        // Add client cards
        for (UsuarioDTO usuario : clients) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-card.fxml"));
                Parent clientCard = loader.load();

                // Get controller and set client data
                ClientCardController cardController = loader.getController();
                cardController.setParentController(this);
                cardController.setUsuario(usuario);

                // Add card to container
                clientsContainer.getChildren().add(clientCard);

                // Add to tracking list for selection management
                clientCards.add(clientCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to filter clients based on search text
    private void applyFilter(String searchText) throws IOException {
        if (searchText == null || searchText.isEmpty()) {
            // If search is empty, show all clients
            List<UsuarioDTO> usuarioDTOList = implRetroFit.getUsuarios();
            displayClients(usuarioDTOList);
            return;
        }

        // Convert search text to lowercase for case-insensitive search
        searchText = searchText.toLowerCase();

        // Get all clients
        List<UsuarioDTO> allClients = implRetroFit.getUsuarios();
        List<UsuarioDTO> filteredClients = new ArrayList<>();

        // Filter clients based on search text
        for (UsuarioDTO usuario : allClients) {
            if (usuario.getNombre().toLowerCase().contains(searchText) ||
                usuario.getApellidos().toLowerCase().contains(searchText) ||
                usuario.getCorreo().toLowerCase().contains(searchText) ||
                usuario.getDni().toLowerCase().contains(searchText) ||
                usuario.getUserName().toLowerCase().contains(searchText)) {
                filteredClients.add(usuario);
            }
        }

        // Display filtered clients
        displayClients(filteredClients);
    }

    // Method to set the selected client (will be called from ClientCardController)
    public void setSelectedUsuario(UsuarioDTO usuario, Parent card) {
        this.selectedUsuario = usuario;

        // Clear selection from all cards
        for (Parent clientCard : clientCards) {
            clientCard.getStyleClass().remove("document-card-selected");
        }

        // Add selection to the clicked card
        if (card != null) {
            card.getStyleClass().add("document-card-selected");
        }
    }

    public void goToUpdate(ActionEvent event) {
        try {
            if (selectedUsuario == null) {
                AlertController.showAlert("Advertencia", "Por favor, seleccione un cliente primero");
                return;
            }

            updatable = selectedUsuario;

            // Acceso al stage actual
            MenuItem menuItem = (MenuItem) event.getSource();
            ContextMenu contextMenu = menuItem.getParentPopup();
            Scene scene = contextMenu.getOwnerNode().getScene();
            Stage currentStage = (Stage) scene.getWindow();

            // Store current window dimensions and state
            double width = currentStage.getWidth();
            double height = currentStage.getHeight();
            boolean maximized = currentStage.isMaximized();

            // Calculate the aspect ratio
            double aspectRatio = width / height;

            // If maximized, temporarily set to non-maximized to capture actual size
            if (maximized) {
                currentStage.setMaximized(false);
                // Get the actual window size before maximizing
                width = currentStage.getWidth();
                height = currentStage.getHeight();
                // Recalculate aspect ratio with actual size
                aspectRatio = width / height;
                // Restore maximized state
                currentStage.setMaximized(true);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-update-view.fxml"));
            Parent secondSceneParent = loader.load();

            // Set the preferred size on the root to maintain aspect ratio if it's a Region
            if (secondSceneParent instanceof Region) {
                Region rootRegion = (Region) secondSceneParent;
                rootRegion.setPrefWidth(width);
                rootRegion.setPrefHeight(height);
            }

            Scene secondScene = new Scene(secondSceneParent, width, height);

            // Acceso al controlador de la segunda escena, si es necesario
            ClientUpdateController secondController = loader.getController();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // First set the scene with the correct dimensions
            currentStage.setScene(secondScene);

            // Then restore maximized state if needed
            if (maximized) {
                currentStage.setMaximized(true);
            } else {
                // Ensure correct dimensions for non-maximized state
                currentStage.setWidth(width);
                currentStage.setHeight(height);
            }

            // Create a final copy of the aspect ratio for use in lambda expressions
            final double finalAspectRatio = aspectRatio;

            // Add a listener to maintain aspect ratio when the window is resized
            currentStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                if (!currentStage.isMaximized()) {
                    currentStage.setHeight(newVal.doubleValue() / finalAspectRatio);
                }
            });

            currentStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                if (!currentStage.isMaximized()) {
                    currentStage.setWidth(newVal.doubleValue() * finalAspectRatio);
                }
            });

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent event) throws IOException {
        if (selectedUsuario == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un cliente primero");
            return;
        }

        implRetroFit.deleteUsuario(selectedUsuario);

        // Reload and display clients
        List<UsuarioDTO> usuarioDTOList = implRetroFit.getUsuarios();
        displayClients(usuarioDTOList);
    }

    public void goToHep(ActionEvent event) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI("https://concrete-binder-4b1.notion.site/Clientes-de41359ba305459988a3876795bad890");
            desktop.browse(uri);
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error al abrir la URL: " + e.getMessage());
        }
    }

    public void subir(ActionEvent event) {
        if (selectedUsuario == null) {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un cliente primero");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Busca la imagen");

        // Agregar filtros de extensión si es necesario
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        ImagenDTO imagenDTO = new ImagenDTO();
        UsuarioDTO selectedJardin = selectedUsuario;

        if (selectedJardin != null) {
            // Mostrar el FileChooser y capturar el archivo seleccionado
            File selectedFile = fileChooser.showOpenDialog(currentStage);
            if (selectedFile != null) {
                try {
                    FileUpload imagenUploadDto = new FileUpload();
                    // Leer el contenido del archivo en un arreglo de bytes
                    byte[] fileContent = Files.readAllBytes(selectedFile.toPath());

                    imagenDTO.setIdJardin(selectedJardin.getId());
                    imagenDTO.setIdUsuario(selectedJardin.getId());
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    imagenUploadDto.setContent(encodedString);
                    imagenUploadDto.setFileName(selectedFile.getName());
                    imagenUploadDto.setFileType(Files.probeContentType(selectedFile.toPath()));
                    imagenUploadDto.setId(imagenDTO.getId());

                    System.out.println(imagenUploadDto.getFileName());

                    implRetroFit.uploadFacturas(imagenUploadDto);

                    // Aquí puedes guardar la cadena codificada en Base64 en algún lugar si lo necesitas
                } catch (IOException e) {
                    e.printStackTrace();
                    AlertController.showAlert("File Error", "Could not read the file: " + e.getMessage());
                }
            }
        }
    }
}
