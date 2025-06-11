package com.example.fxtry.Controller;

import com.example.fxtry.Model.ConversacionDTO;
import com.example.fxtry.Model.MensajeDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.fxtry.Controller.LoginController.admin;

public class MensajeriaController implements Initializable {

    @FXML
    private ListView<ConversacionDTO> conversacionesListView;

    @FXML
    private VBox mensajesContainer;

    @FXML
    private TextArea mensajeTextArea;

    @FXML
    private Button enviarButton;

    @FXML
    private Label conversacionTitleLabel;

    @FXML
    private Button backButton;

    @FXML
    private ScrollPane mensajesScrollPane;

    private ImplRetroFit implRetroFit;
    private ConversacionDTO conversacionSeleccionada;
    private ObservableList<ConversacionDTO> conversacionesObservable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        implRetroFit = new ImplRetroFit();
        conversacionesObservable = FXCollections.observableArrayList();

        // Configurar la lista de conversaciones
        conversacionesListView.setItems(conversacionesObservable);
        conversacionesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ConversacionDTO conversacion, boolean empty) {
                super.updateItem(conversacion, empty);
                if (empty || conversacion == null) {
                    setText(null);
                } else {
                    setText(conversacion.getNombreUsuario() + " - " + conversacion.getTitulo());
                }
            }
        });

        // Listener para selección de conversación
        conversacionesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                conversacionSeleccionada = newValue;
                // Actualizar el título de la conversación
                if (conversacionTitleLabel != null) {
                    conversacionTitleLabel.setText("Conversación con " + newValue.getNombreUsuario());
                }
                cargarMensajes();
            }
        });

        // Configurar botón de enviar
        enviarButton.setOnAction(event -> enviarMensaje());

        // Cargar conversaciones
        cargarConversaciones();
    }

    private void cargarConversaciones() {
        try {
            List<ConversacionDTO> conversaciones;

            // Si es administrador, cargar todas las conversaciones
            if (admin.getRol().equalsIgnoreCase("ADMIN")) {
                conversaciones = implRetroFit.getAllConversaciones();
            } else {
                // Si es cliente, cargar solo su conversación
                ConversacionDTO conversacion = implRetroFit.getConversacionByUsuario(admin.getId());
                conversaciones = List.of(conversacion);
            }

            conversacionesObservable.clear();
            conversacionesObservable.addAll(conversaciones);

            // Seleccionar la primera conversación si hay alguna
            if (!conversaciones.isEmpty()) {
                conversacionesListView.getSelectionModel().select(0);
            }
        } catch (Exception e) {
            mostrarAlerta("Error al cargar conversaciones", e.getMessage());
        }
    }

    @FXML
    public void cargarMensajes() {
        if (conversacionSeleccionada == null) {
            return;
        }

        try {
            // Obtener la conversación actualizada
            ConversacionDTO conversacion = implRetroFit.getConversacionById(conversacionSeleccionada.getId());

            // Actualizar el título de la conversación
            if (conversacionTitleLabel != null) {
                conversacionTitleLabel.setText("Conversación con " + conversacion.getNombreUsuario());
            }

            // Limpiar el contenedor de mensajes
            mensajesContainer.getChildren().clear();

            // Agregar cada mensaje al contenedor
            for (MensajeDTO mensaje : conversacion.getMensajes()) {
                Label mensajeLabel = new Label();

                // Formatear el mensaje con timestamp
                String timestamp = "";
                if (mensaje.getFechaEnvio() != null) {
                    timestamp = " [" + mensaje.getFechaEnvio().toLocalDateTime().toLocalTime().toString().substring(0, 5) + "]";
                }
                String textoMensaje = mensaje.getNombreEmisor() + " (" + mensaje.getRolEmisor() + ")" + timestamp + ": " + mensaje.getContenido();
                mensajeLabel.setText(textoMensaje);

                // Aplicar estilo según si es mensaje propio o no
                if (mensaje.getIdEmisor().equals(admin.getId())) {
                    mensajeLabel.getStyleClass().add("mensaje-propio");
                    HBox container = new HBox();
                    container.setAlignment(Pos.CENTER_RIGHT);
                    container.setPadding(new Insets(5, 5, 5, 5));
                    container.getChildren().add(mensajeLabel);
                    mensajesContainer.getChildren().add(container);
                } else {
                    mensajeLabel.getStyleClass().add("mensaje-otro");
                    HBox container = new HBox();
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.setPadding(new Insets(5, 5, 5, 5));
                    container.getChildren().add(mensajeLabel);
                    mensajesContainer.getChildren().add(container);
                }
            }

            // Marcar mensajes como leídos
            implRetroFit.marcarComoLeidos(conversacion.getId(), admin.getId());

            // Scroll to bottom after messages are loaded
            Platform.runLater(() -> {
                mensajesScrollPane.setVvalue(1.0);
            });
        } catch (Exception e) {
            mostrarAlerta("Error al cargar mensajes", e.getMessage());
        }
    }

    private void enviarMensaje() {
        if (conversacionSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una conversación");
            return;
        }

        String contenido = mensajeTextArea.getText().trim();
        if (contenido.isEmpty()) {
            mostrarAlerta("Error", "El mensaje no puede estar vacío");
            return;
        }

        try {
            // Crear el mensaje
            MensajeDTO mensaje = new MensajeDTO();
            mensaje.setIdConversacion(conversacionSeleccionada.getId());
            mensaje.setIdEmisor(admin.getId());
            mensaje.setNombreEmisor(admin.getNombre() + " " + admin.getApellidos());
            mensaje.setRolEmisor(admin.getRol());
            mensaje.setContenido(contenido);
            mensaje.setFechaEnvio(ZonedDateTime.now());
            mensaje.setLeido(false);

            // Enviar el mensaje
            implRetroFit.enviarMensaje(mensaje);

            // Limpiar el área de texto
            mensajeTextArea.clear();

            // Recargar los mensajes
            cargarMensajes();

            // Asegurarse de que el foco vuelva al área de texto
            Platform.runLater(() -> {
                mensajeTextArea.requestFocus();
            });
        } catch (Exception e) {
            mostrarAlerta("Error al enviar mensaje", e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the main view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Parent mainViewParent = loader.load();
            Scene mainViewScene = new Scene(mainViewParent);

            // Apply CSS
            mainViewScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());

            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the main view scene
            currentStage.setScene(mainViewScene);
            currentStage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo volver a la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
