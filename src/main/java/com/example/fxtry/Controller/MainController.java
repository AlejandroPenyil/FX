package com.example.fxtry.Controller;

import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.PresupuestosDTO;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private TableView<UsuarioDTO> usuariosTableView;

    @FXML
    private TableColumn<UsuarioDTO, Integer> idColumn;

    @FXML
    private TableColumn<UsuarioDTO, String> userNameColumn;

    @FXML
    private TableColumn<UsuarioDTO, String> nombreColumn;

    @FXML
    private TableColumn<UsuarioDTO, String> apellidosColumn;

    @FXML
    private TableColumn<UsuarioDTO, String> correoColumn;

    @FXML
    private TableColumn<UsuarioDTO, String> dniColumn;

    @FXML
    private TableColumn<UsuarioDTO, String> telefonoColumn;

    @FXML
    private TableColumn<UsuarioDTO, String> rolColumn;

    @FXML
    private Label clientesCountLabel;

    @FXML
    private Label jardinesCountLabel;

    @FXML
    private Label solicitudesCountLabel;

    @FXML
    private Label presupuestosCountLabel;

    private ImplRetroFit implRetroFit = new ImplRetroFit();

    @FXML
    private void initialize() {
        // Initialize the main view
        setupUsuariosTable();

        // Set initial values for dashboard counters
        clientesCountLabel.setText("Cargando...");
        jardinesCountLabel.setText("Cargando...");
        solicitudesCountLabel.setText("Cargando...");
        presupuestosCountLabel.setText("Cargando...");

        // Load data asynchronously
        loadUsuariosAsync();
        loadDashboardDataAsync();
    }

    private void loadDashboardDataAsync() {
        // Create a new thread for loading dashboard data
        Thread thread = new Thread(() -> {
            try {
                // Load clients count
                List<UsuarioDTO> usuarios = implRetroFit.getUsuarios();
                updateLabelOnUIThread(clientesCountLabel, String.valueOf(usuarios.size()));

                // Load gardens count
                List<JardinesDTO> jardines = implRetroFit.getJardines();
                updateLabelOnUIThread(jardinesCountLabel, String.valueOf(jardines.size()));

                // Load requests count
                List<SolicitudDTO> solicitudes = implRetroFit.getSolicitudes();
                updateLabelOnUIThread(solicitudesCountLabel, String.valueOf(solicitudes.size()));

                // Load budgets count
                List<PresupuestosDTO> presupuestos = implRetroFit.getPresupuestos();
                updateLabelOnUIThread(presupuestosCountLabel, String.valueOf(presupuestos.size()));
            } catch (Exception e) {
                e.printStackTrace();
                // Update UI with error message
                updateLabelOnUIThread(clientesCountLabel, "Error");
                updateLabelOnUIThread(jardinesCountLabel, "Error");
                updateLabelOnUIThread(solicitudesCountLabel, "Error");
                updateLabelOnUIThread(presupuestosCountLabel, "Error");
            }
        });

        // Set as daemon thread so it doesn't prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
    }

    private void updateLabelOnUIThread(Label label, String text) {
        javafx.application.Platform.runLater(() -> {
            label.setText(text);
        });
    }

    private void setupUsuariosTable() {
        // Set up cell value factories for each column
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        correoColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));
        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        rolColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));
    }

    private void loadUsuariosAsync() {
        // Create a new thread for loading users data
        Thread thread = new Thread(() -> {
            try {
                // Get users from the service
                List<UsuarioDTO> usuarios = implRetroFit.getUsuarios();

                // Update UI on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    try {
                        // Convert to observable list and set to table
                        ObservableList<UsuarioDTO> usuariosObservable = FXCollections.observableArrayList(usuarios);
                        usuariosTableView.setItems(usuariosObservable);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Could show an alert dialog here
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                // Update UI with error message on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    // Could show an alert dialog here
                });
            }
        });

        // Set as daemon thread so it doesn't prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Helper method to preserve window dimensions when switching scenes
     * @param event The action event
     * @param fxmlPath The path to the FXML file
     */
    private void switchScenePreservingSize(ActionEvent event, String fxmlPath) {
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

            // Load the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Set the preferred size on the root to maintain aspect ratio if it's a Region
            if (root instanceof Region) {
                Region rootRegion = (Region) root;
                rootRegion.setPrefWidth(width);
                rootRegion.setPrefHeight(height);
            }

            Scene scene = new Scene(root, width, height);

            // Apply stylesheet
            scene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // First set the scene with the correct dimensions
            currentStage.setScene(scene);

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

    @FXML
    private void goToClient(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Client/client-view.fxml");
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/login-view.fxml");
    }

    @FXML
    private void goToJardines(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/jardin/jardines-view.fxml");
    }

    @FXML
    private void goToPresupuestos(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Presupuestos/presupuestos-view.fxml");
    }

    @FXML
    private void goToPeticiones(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Peticiones/peticiones-view.fxml");
    }

    @FXML
    private void goToImagenes(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Imagenes/imagenes-view.fxml");
    }

    @FXML
    private void goToFacturas(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Facturas/facturas-view.fxml");
    }

    @FXML
    private void goToActivos(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Activos/activos-view.fxml");
    }

    @FXML
    private void goToDocumentos(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Documentos/documentos-view.fxml");
    }

    @FXML
    private void goToUsuarios(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/Client/client-view.fxml");
    }

    @FXML
    private void goToMensajeria(ActionEvent event) {
        switchScenePreservingSize(event, "/com/example/fxtry/mensajeria-view.fxml");
    }
}
