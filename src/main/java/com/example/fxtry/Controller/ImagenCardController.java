package com.example.fxtry.Controller;

import com.example.fxtry.Model.ImagenDTO;
import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ImagenCardController {

    @FXML
    private HBox imagenCard;

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