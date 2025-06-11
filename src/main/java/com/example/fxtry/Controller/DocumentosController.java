package com.example.fxtry.Controller;

import com.example.fxtry.Model.DocumentoDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DocumentosController extends Application {
    public static DocumentoDTO updatable = new DocumentoDTO();

    ImplRetroFit implRetroFit;

    @FXML
    private VBox documentsContainer;

    @FXML
    private ComboBox<String> filterCategory;

    @FXML
    private ComboBox<String> filterType;

    @FXML
    private TextField searchField;

    private List<DocumentoDTO> allDocuments = new ArrayList<>();

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

        // Initialize filter ComboBoxes
        ObservableList<String> categories = FXCollections.observableArrayList(
            "Todas", "Contratos", "Facturas", "Informes", "Otros"
        );
        filterCategory.setItems(categories);
        filterCategory.setValue("Todas");

        ObservableList<String> types = FXCollections.observableArrayList(
            "Todos", "PDF", "DOCX", "XLSX", "JPG", "PNG", "TXT"
        );
        filterType.setItems(types);
        filterType.setValue("Todos");

        // Add listeners for filters
        filterCategory.setOnAction(e -> applyFilters());
        filterType.setOnAction(e -> applyFilters());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Load sample data
        loadSampleData();

        // Display documents
        displayDocuments(allDocuments);
    }

    private void loadSampleData() {
        // Add sample documents
        DocumentoDTO doc1 = new DocumentoDTO();
        doc1.setId(1L);
        doc1.setNombre("Contrato de mantenimiento.pdf");
        doc1.setTipo("PDF");
        doc1.setFechaCreacion("2023-05-20");
        doc1.setTamaño(1024L * 1024); // 1MB
        doc1.setUsuario("admin");
        doc1.setCategoria("Contratos");
        allDocuments.add(doc1);

        DocumentoDTO doc2 = new DocumentoDTO();
        doc2.setId(2L);
        doc2.setNombre("Factura 2023-001.xlsx");
        doc2.setTipo("XLSX");
        doc2.setFechaCreacion("2023-06-15");
        doc2.setTamaño(512L * 1024); // 512KB
        doc2.setUsuario("contabilidad");
        doc2.setCategoria("Facturas");
        allDocuments.add(doc2);

        DocumentoDTO doc3 = new DocumentoDTO();
        doc3.setId(3L);
        doc3.setNombre("Informe trimestral.docx");
        doc3.setTipo("DOCX");
        doc3.setFechaCreacion("2023-07-01");
        doc3.setTamaño(2048L * 1024); // 2MB
        doc3.setUsuario("gerencia");
        doc3.setCategoria("Informes");
        allDocuments.add(doc3);

        DocumentoDTO doc4 = new DocumentoDTO();
        doc4.setId(4L);
        doc4.setNombre("Foto jardín cliente.jpg");
        doc4.setTipo("JPG");
        doc4.setFechaCreacion("2023-07-10");
        doc4.setTamaño(3072L * 1024); // 3MB
        doc4.setUsuario("tecnico");
        doc4.setCategoria("Otros");
        allDocuments.add(doc4);
    }

    private void displayDocuments(List<DocumentoDTO> documents) {
        // Clear existing documents
        documentsContainer.getChildren().clear();

        // Clear tracking list
        documentCards.clear();

        // Reset selected document
        selectedDocumento = null;

        // Add document cards
        for (DocumentoDTO documento : documents) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Documentos/document-card.fxml"));
                Parent documentCard = loader.load();

                // Get controller and set document data
                DocumentCardController cardController = loader.getController();
                cardController.setParentController(this);
                cardController.setDocumento(documento);

                // Add card to container
                documentsContainer.getChildren().add(documentCard);

                // Add to tracking list for selection management
                documentCards.add(documentCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyFilters() {
        String categoryFilter = filterCategory.getValue();
        String typeFilter = filterType.getValue();
        String searchText = searchField.getText().toLowerCase();

        List<DocumentoDTO> filteredDocuments = new ArrayList<>();

        for (DocumentoDTO documento : allDocuments) {
            boolean matchesCategory = categoryFilter.equals("Todas") || documento.getCategoria().equals(categoryFilter);
            boolean matchesType = typeFilter.equals("Todos") || documento.getTipo().equals(typeFilter);
            boolean matchesSearch = searchText.isEmpty() || 
                                   documento.getNombre().toLowerCase().contains(searchText) ||
                                   documento.getUsuario().toLowerCase().contains(searchText);

            if (matchesCategory && matchesType && matchesSearch) {
                filteredDocuments.add(documento);
            }
        }

        displayDocuments(filteredDocuments);
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar documento");

        // Add extension filters if needed
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Documents", "*.docx"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Show FileChooser and capture selected file
        File selectedFile = fileChooser.showOpenDialog(currentStage);
        if (selectedFile != null) {
            AlertController.showAlert("Información", "Documento seleccionado: " + selectedFile.getName() + 
                                     "\nFuncionalidad de carga en desarrollo");
        }
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
            URI uri = new URI("https://concrete-binder-4b1.notion.site/Documentos-9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d");
            desktop.browse(uri);
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error al abrir la URL: " + e.getMessage());
        }
    }

    // Track the currently selected document
    private DocumentoDTO selectedDocumento = null;

    // Track all document cards for selection management
    private List<Parent> documentCards = new ArrayList<>();

    // Method to set the selected document (will be called from DocumentCardController)
    public void setSelectedDocumento(DocumentoDTO documento, Parent card) {
        this.selectedDocumento = documento;

        // Clear selection from all cards
        for (Parent documentCard : documentCards) {
            documentCard.getStyleClass().remove("document-card-selected");
        }

        // Add selection to the clicked card
        if (card != null) {
            card.getStyleClass().add("document-card-selected");
        }
    }

    @FXML
    private void descargarDocumento(ActionEvent event) {
        if (selectedDocumento != null) {
            // In a real application, you would get the document content from the server
            // For now, just show an alert
            AlertController.showAlert("Información", "Descarga de documento: " + selectedDocumento.getNombre());
        } else {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un documento primero");
        }
    }

    @FXML
    private void previsualizarDocumento(ActionEvent event) {
        if (selectedDocumento != null) {
            // In a real application, you would preview the document
            // For now, just show an alert
            AlertController.showAlert("Información", "Previsualización de documento: " + selectedDocumento.getNombre());
        } else {
            AlertController.showAlert("Advertencia", "Por favor, seleccione un documento primero");
        }
    }
}
