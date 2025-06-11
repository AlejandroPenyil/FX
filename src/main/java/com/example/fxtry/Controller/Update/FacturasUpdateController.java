package com.example.fxtry.Controller.Update;

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
import java.util.List;

import static com.example.fxtry.Controller.FacturasController.updatable;

public class FacturasUpdateController {

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
            
            // Load existing data if updatable is set
            if (updatable != null) {
                // Set the invoice number
                txtNumeroFactura.setText(updatable.getNumeroFactura());
                
                // Set the location
                txtUbicacion.setText(updatable.getUbicacion());
                
                // Set the financial values
                if (updatable.getTotal() != null) {
                    txtTotal.setText(updatable.getTotal().toString());
                }
                
                if (updatable.getSubtotal() != null) {
                    txtSubtotal.setText(updatable.getSubtotal().toString());
                }
                
                if (updatable.getIva() != null) {
                    txtIva.setText(updatable.getIva().toString());
                }
                
                if (updatable.getDescuento() != null) {
                    txtDescuento.setText(updatable.getDescuento().toString());
                }
                
                // Set the notes
                txtNotas.setText(updatable.getNotas());
                
                // Select the client in the combo box
                if (updatable.getIdCliente() != null) {
                    try {
                        UsuarioDTO cliente = implRetroFit.getUsuario(updatable.getIdCliente());
                        if (cliente != null) {
                            for (UsuarioDTO u : cmbCliente.getItems()) {
                                if (u.getId().equals(cliente.getId())) {
                                    cmbCliente.setValue(u);
                                    break;
                                }
                            }
                        }
                    } catch (IOException e) {
                        lblError.setText("Error al cargar cliente: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            lblError.setText("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void update(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            if (updatable == null) {
                lblError.setText("No hay factura para actualizar.");
                return;
            }
            
            // Set the client ID
            UsuarioDTO selectedCliente = cmbCliente.getValue();
            if (selectedCliente != null) {
                updatable.setIdCliente(selectedCliente.getId());
            }
            
            // Set the invoice number
            updatable.setNumeroFactura(txtNumeroFactura.getText().trim());
            
            // Set the location
            updatable.setUbicacion(txtUbicacion.getText().trim());
            
            // Set the financial values
            try {
                updatable.setTotal(new BigDecimal(txtTotal.getText().trim()));
                updatable.setSubtotal(new BigDecimal(txtSubtotal.getText().trim()));
                updatable.setIva(new BigDecimal(txtIva.getText().trim()));
                updatable.setDescuento(new BigDecimal(txtDescuento.getText().trim()));
            } catch (NumberFormatException e) {
                lblError.setText("Por favor, ingrese valores numéricos válidos para los campos monetarios.");
                return;
            }
            
            // Set the notes
            updatable.setNotas(txtNotas.getText().trim());

            // Update the factura
            implRetroFit.updateFactura(updatable.getId(), updatable);

            AlertController.showInformation("Factura Actualizada", "La factura ha sido actualizada correctamente.");
            navigateToFacturasView(event);
        } catch (IOException e) {
            lblError.setText("Error al actualizar la factura: " + e.getMessage());
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