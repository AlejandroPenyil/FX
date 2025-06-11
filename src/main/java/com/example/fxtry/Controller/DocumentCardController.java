package com.example.fxtry.Controller;

import com.example.fxtry.Model.DocumentoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DocumentCardController {

    @FXML
    private HBox documentCard;

    @FXML
    private ImageView docIcon;

    @FXML
    private Label docName;

    @FXML
    private Label docType;

    @FXML
    private Label docSize;

    @FXML
    private Label docDate;

    @FXML
    private Label docCategory;

    @FXML
    private Label docUser;

    private DocumentoDTO documento;

    // Reference to the parent controller
    private DocumentosController parentController;

    // Map to store document type icons
    private static final Map<String, String> DOCUMENT_ICONS = new HashMap<>();

    static {
        // Initialize with default icons - in a real app, you would use actual icon paths
        DOCUMENT_ICONS.put("PDF", "/images/pdf_icon.png");
        DOCUMENT_ICONS.put("DOCX", "/images/word_icon.png");
        DOCUMENT_ICONS.put("XLSX", "/images/excel_icon.png");
        DOCUMENT_ICONS.put("JPG", "/images/image_icon.png");
        DOCUMENT_ICONS.put("PNG", "/images/image_icon.png");
        DOCUMENT_ICONS.put("TXT", "/images/text_icon.png");
        // Default icon for unknown types
        DOCUMENT_ICONS.put("DEFAULT", "/images/document_icon.png");
    }

    public void setDocumento(DocumentoDTO documento) {
        this.documento = documento;

        // Set document name
        docName.setText(documento.getNombre());

        // Set document type
        String tipo = documento.getTipo();
        docType.setText(tipo);

        // Set document size (format as KB, MB, etc.)
        long size = documento.getTamaño();
        docSize.setText(formatFileSize(size));

        // Set document date
        docDate.setText(documento.getFechaCreacion());

        // Set document category
        docCategory.setText(documento.getCategoria());

        // Set document user
        docUser.setText(documento.getUsuario());

        // Set document icon based on type
        setDocumentIcon(tipo);

        // Add click event handler to the card
        documentCard.setOnMouseClicked(this::handleCardClick);
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent DocumentosController
     */
    public void setParentController(DocumentosController controller) {
        this.parentController = controller;
    }

    /**
     * Handles click events on the document card
     * @param event The mouse event
     */
    private void handleCardClick(MouseEvent event) {
        // Notify the parent controller about the selection
        if (parentController != null) {
            parentController.setSelectedDocumento(documento, documentCard);
        }
    }

    private void setDocumentIcon(String tipo) {
        // For now, use a placeholder icon since we don't have actual icons
        // In a real app, you would use the DOCUMENT_ICONS map to get the appropriate icon
        try {
            // Use default icon for now
            docIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))));
        } catch (Exception e) {
            System.err.println("Error loading document icon: " + e.getMessage());
        }
    }

    private String formatFileSize(long size) {
        // Format file size as KB, MB, etc.
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
        }
    }

    public HBox getDocumentCard() {
        return documentCard;
    }
}
