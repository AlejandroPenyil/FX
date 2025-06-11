package com.example.fxtry.Controller;

import com.example.fxtry.Model.JardinesDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class JardinCardController {

    @FXML
    private HBox jardinCard;

    @FXML
    private Label jardinLocalizacion;

    @FXML
    private Label jardinTamano;

    @FXML
    private Label jardinCliente;

    private JardinesDTO jardin;

    // Reference to the parent controller
    private Object parentController;

    public void setJardin(JardinesDTO jardin) {
        this.jardin = jardin;

        // Set garden location
        jardinLocalizacion.setText(jardin.getLocalizacion());

        // Set garden size
        jardinTamano.setText(jardin.getTamaño().toString() + " m²");

        // Set garden client ID
        jardinCliente.setText("Cliente ID: " + jardin.getIdCliente().toString());

        // Add click event handler to the card
        jardinCard.setOnMouseClicked(this::handleCardClick);
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent JardinesCardView controller
     */
    public void setParentController(JardinesCardView controller) {
        this.parentController = controller;
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent JardinesController controller
     */
    public void setParentController(JardinesController controller) {
        this.parentController = controller;
    }

    /**
     * Handles click events on the garden card
     * @param event The mouse event
     */
    private void handleCardClick(MouseEvent event) {
        // Notify the parent controller about the selection
        if (parentController != null) {
            if (parentController instanceof JardinesCardView) {
                ((JardinesCardView) parentController).setSelectedJardin(jardin, jardinCard);
            } else if (parentController instanceof JardinesController) {
                ((JardinesController) parentController).setSelectedJardin(jardin, jardinCard);
            }
        }
    }

    public HBox getJardinCard() {
        return jardinCard;
    }
}
