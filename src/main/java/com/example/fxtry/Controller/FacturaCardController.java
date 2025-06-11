package com.example.fxtry.Controller;

import com.example.fxtry.Model.FacturaDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class FacturaCardController {

    @FXML
    private HBox facturaCard;

    @FXML
    private Label facturaId;

    @FXML
    private Label facturaCliente;

    @FXML
    private Label facturaFecha;

    private FacturaDTO factura;

    // Reference to the parent controller
    private FacturasController parentController;

    // Reference to the retrofit implementation
    private ImplRetroFit implRetroFit;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();
    }

    public void setFactura(FacturaDTO factura) {
        this.factura = factura;

        // Set factura ID
        facturaId.setText(factura.getId().toString());

        // Set factura fecha
        facturaFecha.setText(String.valueOf(factura.getFecha()));

        // Set factura cliente
        try {
            UsuarioDTO cliente = implRetroFit.getUsuario(factura.getIdCliente());
            if (cliente != null) {
                facturaCliente.setText(cliente.getNombre() + " " + cliente.getApellidos());
            } else {
                facturaCliente.setText("Cliente desconocido");
            }
        } catch (IOException e) {
            e.printStackTrace();
            facturaCliente.setText("Error al cargar cliente");
        }

        // Add click event handler to the card
        facturaCard.setOnMouseClicked(this::handleCardClick);
    }

    /**
     * Sets the parent controller for this card
     * @param controller The parent FacturasController
     */
    public void setParentController(FacturasController controller) {
        this.parentController = controller;
    }

    /**
     * Handles click events on the factura card
     * @param event The mouse event
     */
    private void handleCardClick(MouseEvent event) {
        // Notify the parent controller about the selection
        if (parentController != null) {
            parentController.setSelectedFactura(factura, facturaCard);
        }
    }

    public HBox getFacturaCard() {
        return facturaCard;
    }
}
