package com.example.fxtry.Controller;

import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class PeticionCardController {

    @FXML
    private HBox peticionCard;

    @FXML
    private Label peticionId;

    @FXML
    private Label peticionCliente;

    @FXML
    private Label peticionFecha;

    @FXML
    private Label peticionAtendida;

    @FXML
    private Label peticionDescripcion;

    private SolicitudDTO peticion;

    // Reference to the parent controller
    private PeticionesController parentController;

    // Reference to the retrofit implementation
    private ImplRetroFit implRetroFit;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();
    }

    public void setPeticion(SolicitudDTO peticion) {
        this.peticion = peticion;

        // Set peticion ID
        peticionId.setText(peticion.getId().toString());

        // Set peticion fecha
        peticionFecha.setText(String.valueOf(peticion.getFechaSolicitud()));

        // Set peticion atendida
        peticionAtendida.setText(peticion.isAtendida() ? "Sí" : "No");

        // Set peticion descripcion
        peticionDescripcion.setText(peticion.getDescripcion());

        // Set peticion cliente
        try {
            UsuarioDTO cliente = implRetroFit.getUsuario(peticion.getIdUsuario());
            if (cliente != null) {
                peticionCliente.setText(cliente.getNombre() + " " + cliente.getApellidos());
            } else {
                peticionCliente.setText("Cliente desconocido");
            }
        } catch (IOException e) {
            e.printStackTrace();
            peticionCliente.setText("Error al cargar cliente");
        }

        // Add click event handler to the card
        peticionCard.setOnMouseClicked(this::handleCardClick);
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent PeticionesController
     */
    public void setParentController(PeticionesController controller) {
        this.parentController = controller;
    }

    /**
     * Handles click events on the peticion card
     * @param event The mouse event
     */
    private void handleCardClick(MouseEvent event) {
        // Notify the parent controller about the selection
        if (parentController != null) {
            parentController.setSelectedPeticion(peticion, peticionCard);
        }
    }

    public HBox getPeticionCard() {
        return peticionCard;
    }
}