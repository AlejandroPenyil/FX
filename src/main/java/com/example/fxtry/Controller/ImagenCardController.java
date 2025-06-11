package com.example.fxtry.Controller;

import com.example.fxtry.Model.ImagenDTO;
import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImagenCardController {

    @FXML
    private HBox imagenCard;

    @FXML
    private ImageView imageView;

    @FXML
    private Label imagenId;

    @FXML
    private Label imagenCliente;

    @FXML
    private Label imagenFecha;

    @FXML
    private Label imagenJardin;

    @FXML
    private Label imagenUrl;

    @FXML
    private Label imagenComentario;

    private ImagenDTO imagen;

    // Reference to the parent controller
    private ImagenesController parentController;

    // Reference to the retrofit implementation
    private ImplRetroFit implRetroFit;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();
    }

    public void setImagen(ImagenDTO imagen) {
        this.imagen = imagen;

        // Set imagen ID
        imagenId.setText(imagen.getId().toString());

        // Set imagen fecha
        imagenFecha.setText(imagen.getFecha());

        // Set imagen URL
        imagenUrl.setText(imagen.getUbicacion());

        // Set imagen comentario
        imagenComentario.setText(imagen.getComentario());

        // Set imagen cliente
        try {
            UsuarioDTO cliente = implRetroFit.getUsuario(imagen.getIdUsuario());
            if (cliente != null) {
                imagenCliente.setText(cliente.getNombre() + " " + cliente.getApellidos());
            } else {
                imagenCliente.setText("Cliente desconocido");
            }
        } catch (IOException e) {
            e.printStackTrace();
            imagenCliente.setText("Error al cargar cliente");
        }

        // Set imagen jardín
        try {
            JardinesDTO jardin = implRetroFit.getJardine(imagen.getIdJardin());
            if (jardin != null) {
                imagenJardin.setText(jardin.getLocalizacion());
            } else {
                imagenJardin.setText("Jardín desconocido");
            }
        } catch (IOException e) {
            e.printStackTrace();
            imagenJardin.setText("Error al cargar jardín");
        }

        // Display the image from Base64 data
        if (imagen.getImageDataBase64() != null && !imagen.getImageDataBase64().isEmpty()) {
            try {
                byte[] imageData = Base64.getDecoder().decode(imagen.getImageDataBase64());
                Image image = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error loading image: " + e.getMessage());
            }
        } else {
            // Set a placeholder or default image if no image data is available
            imageView.setImage(null);
        }

        // Add click event handler to the card
        imagenCard.setOnMouseClicked(this::handleCardClick);
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent ImagenesController
     */
    public void setParentController(ImagenesController controller) {
        this.parentController = controller;
    }

    /**
     * Handles click events on the imagen card
     * @param event The mouse event
     */
    private void handleCardClick(MouseEvent event) {
        // Notify the parent controller about the selection
        if (parentController != null) {
            parentController.setSelectedImagen(imagen, imagenCard);
        }
    }

    public HBox getImagenCard() {
        return imagenCard;
    }
}
