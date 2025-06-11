package com.example.fxtry.Controller;

import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.PresupuestosDTO;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class PresupuestoCardController {

    @FXML
    private HBox presupuestoCard;

    @FXML
    private Label presupuestoId;

    @FXML
    private Label presupuestoJardin;

    @FXML
    private Label presupuestoSolicitud;

    @FXML
    private Label presupuestoRechazado;

    @FXML
    private Label presupuestoUbicacion;

    private PresupuestosDTO presupuesto;

    // Reference to the parent controller
    private PresupuestosController parentController;

    // Reference to the retrofit implementation
    private ImplRetroFit implRetroFit;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();
    }

    public void setPresupuesto(PresupuestosDTO presupuesto) {
        this.presupuesto = presupuesto;

        // Set presupuesto ID
        presupuestoId.setText(presupuesto.getId().toString());

        // Set presupuesto ubicacion
        presupuestoUbicacion.setText(presupuesto.getUbicacion());

        // Set presupuesto rechazado
        presupuestoRechazado.setText(presupuesto.getRechazado() ? "Sí" : "No");

        // Set presupuesto jardin
        try {
            JardinesDTO jardin = implRetroFit.getJardine(presupuesto.getIdJardin());
            if (jardin != null) {
                presupuestoJardin.setText(jardin.getLocalizacion());
            } else {
                presupuestoJardin.setText("Jardín desconocido");
            }
        } catch (IOException e) {
            e.printStackTrace();
            presupuestoJardin.setText("Error al cargar jardín");
        }

        // Set presupuesto solicitud
        try {
            SolicitudDTO solicitud = implRetroFit.getSolicitud(presupuesto.getIdSolicitud());
            if (solicitud != null) {
                UsuarioDTO cliente = implRetroFit.getUsuario(solicitud.getIdUsuario());
                if (cliente != null) {
                    presupuestoSolicitud.setText(cliente.getNombre() + " " + cliente.getApellidos());
                } else {
                    presupuestoSolicitud.setText("Cliente desconocido");
                }
            } else {
                presupuestoSolicitud.setText("Solicitud desconocida");
            }
        } catch (IOException e) {
            e.printStackTrace();
            presupuestoSolicitud.setText("Error al cargar solicitud");
        }

        // Add click event handler to the card
        presupuestoCard.setOnMouseClicked(this::handleCardClick);
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent PresupuestosController
     */
    public void setParentController(PresupuestosController controller) {
        this.parentController = controller;
    }

    /**
     * Handles click events on the presupuesto card
     * @param event The mouse event
     */
    private void handleCardClick(MouseEvent event) {
        // Notify the parent controller about the selection
        if (parentController != null) {
            parentController.setSelectedPresupuesto(presupuesto, presupuestoCard);
        }
    }

    public HBox getPresupuestoCard() {
        return presupuestoCard;
    }
}