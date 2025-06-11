package com.example.fxtry.Controller.Create;

import com.example.fxtry.Controller.AlertController;
import com.example.fxtry.Controller.FacturasController;
import com.example.fxtry.Model.FacturaDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class FacturasCreateController {

    private ImplRetroFit implRetroFit;

    @FXML
    private ComboBox<UsuarioDTO> cmbCliente;

    @FXML
    private TextField txtNumeroFactura;

    @FXML
    private TextField txtUbicacion;

    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtSubtotal;

    @FXML
    private TextField txtIva;

    @FXML
    private TextField txtDescuento;

    @FXML
    private TextArea txtNotas;

    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Load clientes for the combo box
        try {
            List<UsuarioDTO> usuarios = implRetroFit.getUsuarios();
            cmbCliente.getItems().addAll(usuarios);
            
            // Set a custom cell factory to display the client name
            cmbCliente.setCellFactory(param -> new javafx.scene.control.ListCell<UsuarioDTO>() {
                @Override
                protected void updateItem(UsuarioDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNombre() + " " + item.getApellidos());
                    }
                }
            });
            
            // Set the same converter for the display text
            cmbCliente.setConverter(new javafx.util.StringConverter<UsuarioDTO>() {
                @Override
                public String toString(UsuarioDTO usuario) {
                    if (usuario == null) {
                        return null;
                    }
                    return usuario.getNombre() + " " + usuario.getApellidos();
                }

                @Override
                public UsuarioDTO fromString(String string) {
                    return null; // Not needed for this use case
                }
            });
        } catch (IOException e) {
            lblError.setText("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void create(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            FacturaDTO facturaDTO = new FacturaDTO();
            
            // Set the client ID
            UsuarioDTO selectedCliente = cmbCliente.getValue();
            if (selectedCliente != null) {
                facturaDTO.setIdCliente(selectedCliente.getId());
            }
            
            // Set the invoice number
            facturaDTO.setNumeroFactura(txtNumeroFactura.getText().trim());
            
            // Set the location
            facturaDTO.setUbicacion(txtUbicacion.getText().trim());
            
            // Set the financial values
            try {
                facturaDTO.setTotal(new BigDecimal(txtTotal.getText().trim()));
                facturaDTO.setSubtotal(new BigDecimal(txtSubtotal.getText().trim()));
                facturaDTO.setIva(new BigDecimal(txtIva.getText().trim()));
                facturaDTO.setDescuento(new BigDecimal(txtDescuento.getText().trim()));
            } catch (NumberFormatException e) {
                lblError.setText("Por favor, ingrese valores numéricos válidos para los campos monetarios.");
                return;
            }
            
            // Set the notes
            facturaDTO.setNotas(txtNotas.getText().trim());
            
            // Set the current date
            facturaDTO.setFecha(ZonedDateTime.now());

            // Create the factura
            implRetroFit.createFactura(facturaDTO);

            AlertController.showInformation("Factura Creada", "La factura ha sido creada correctamente.");
            navigateToFacturasView(event);
        } catch (IOException e) {
            lblError.setText("Error al crear la factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (cmbCliente.getValue() == null) {
            lblError.setText("Por favor, seleccione un cliente.");
            return false;
        }

        if (isEmpty(txtNumeroFactura.getText())) {
            lblError.setText("Por favor, ingrese un número de factura.");
            return false;
        }

        if (isEmpty(txtTotal.getText())) {
            lblError.setText("Por favor, ingrese un total.");
            return false;
        }

        if (isEmpty(txtSubtotal.getText())) {
            lblError.setText("Por favor, ingrese un subtotal.");
            return false;
        }

        if (isEmpty(txtIva.getText())) {
            lblError.setText("Por favor, ingrese un IVA.");
            return false;
        }

        // Clear any previous error
        lblError.setText("");
        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    @FXML
    private void goToFacturas(ActionEvent event) {
        navigateToFacturasView(event);
    }

    private void navigateToFacturasView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Facturas/facturas-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            FacturasController secondController = loader.getController();

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            currentStage.setScene(secondScene);
            currentStage.show();
        } catch (IOException e) {
            lblError.setText("Error al navegar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}