package com.example.fxtry.Controller;

import com.example.fxtry.Model.ActivoDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ActivosController extends Application {
    public static ActivoDTO updatable = new ActivoDTO();

    ImplRetroFit implRetroFit;

    @FXML
    private TableView<ActivoDTO> tvwActivos;

    @FXML
    private TableColumn<ActivoDTO, String> tcId, tcNombre, tcDescripcion, tcFechaAdquisicion, tcValor, tcEstado, tcUbicacion;

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
    private void initialize() {
        implRetroFit = new ImplRetroFit();
        
        // Configure table columns
        tcId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        tcFechaAdquisicion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaAdquisicion()));
        tcValor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValor().toString()));
        tcEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
        tcUbicacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUbicacion()));
        
        // Load data (commented out until ActivoDTO and service are implemented)
        // List<ActivoDTO> activosList = implRetroFit.getActivos();
        // for (ActivoDTO activo : activosList) {
        //     tvwActivos.getItems().add(activo);
        // }
        
        // For now, add some sample data
        ActivoDTO sampleActivo = new ActivoDTO();
        sampleActivo.setId(1L);
        sampleActivo.setNombre("Equipo de jardinería");
        sampleActivo.setDescripcion("Herramientas para mantenimiento de jardines");
        sampleActivo.setFechaAdquisicion("2023-01-15");
        sampleActivo.setValor(1500.0);
        sampleActivo.setEstado("Activo");
        sampleActivo.setUbicacion("Almacén central");
        
        tvwActivos.getItems().add(sampleActivo);
        tvwActivos.refresh();
    }

    @FXML
    private void goToMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            
            // Get controller if needed
            MainController secondController = loader.getController();

            // Get current stage
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            
            // Show scene in current stage
            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToCreate(ActionEvent event) {
        // To be implemented when create view is available
        AlertController.showAlert("Información", "Funcionalidad en desarrollo");
    }

    @FXML
    private void goToUpdate(ActionEvent event) {
        // To be implemented when update view is available
        AlertController.showAlert("Información", "Funcionalidad en desarrollo");
    }

    @FXML
    private void delete(ActionEvent event) {
        // To be implemented when service is available
        AlertController.showAlert("Información", "Funcionalidad en desarrollo");
    }

    @FXML
    private void goToHelp(ActionEvent event) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI("https://concrete-binder-4b1.notion.site/Activos-de-Jardiner-a-5a3b7c8d9e1f4b2a8c7d6e5f4a3b2c1d");
            desktop.browse(uri);
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error al abrir la URL: " + e.getMessage());
        }
    }

    @FXML
    private void subirDocumento(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar documento");

        // Add extension filters if needed
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        
        ActivoDTO selectedActivo = tvwActivos.getSelectionModel().getSelectedItem();

        if (selectedActivo != null) {
            // Show FileChooser and capture selected file
            File selectedFile = fileChooser.showOpenDialog(currentStage);
            if (selectedFile != null) {
                AlertController.showAlert("Información", "Documento seleccionado: " + selectedFile.getName() + 
                                         "\nFuncionalidad de carga en desarrollo");
            }
        } else {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un activo primero");
        }
    }
}