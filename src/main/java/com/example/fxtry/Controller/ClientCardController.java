package com.example.fxtry.Controller;

import com.example.fxtry.Model.UsuarioDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ClientCardController {

    @FXML
    private HBox clientCard;

    @FXML
    private Label clientId;

    @FXML
    private Label clientName;

    @FXML
    private Label clientEmail;

    @FXML
    private Label clientPhone;

    @FXML
    private Label clientDni;

    @FXML
    private Label clientAddress;

    @FXML
    private Label clientRole;

    @FXML
    private Label clientUsername;

    private UsuarioDTO usuario;

    // Reference to the parent controller
    private ClientController parentController;

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;

        // Set client ID
        clientId.setText(usuario.getId().toString());

        // Set client name (combine nombre and apellidos)
        clientName.setText(usuario.getNombre() + " " + usuario.getApellidos());

        // Set client email
        clientEmail.setText(usuario.getCorreo());

        // Set client phone
        clientPhone.setText(usuario.getTelefono());

        // Set client DNI
        clientDni.setText(usuario.getDni());

        // Set client address
        clientAddress.setText(usuario.getDireccion());

        // Set client role
        clientRole.setText(usuario.getRol());

        // Set client username
        clientUsername.setText(usuario.getUserName());

        // Add click event handler to the card
        clientCard.setOnMouseClicked(this::handleCardClick);
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent ClientController
     */
    public void setParentController(ClientController controller) {
        this.parentController = controller;
    }

    /**
     * Handles click events on the client card
     * @param event The mouse event
     */
    private void handleCardClick(MouseEvent event) {
        // Notify the parent controller about the selection
        if (parentController != null) {
            parentController.setSelectedUsuario(usuario, clientCard);
        }
    }

    public HBox getClientCard() {
        return clientCard;
    }
}