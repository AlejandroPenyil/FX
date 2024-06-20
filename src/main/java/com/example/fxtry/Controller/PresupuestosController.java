package com.example.fxtry.Controller;

import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.PresupuestosDTO;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.example.fxtry.Controller.AlertController.showAlert;

public class PresupuestosController {
    ImplRetroFit implRetroFit;

    @FXML
    private TableView<PresupuestosDTO> tvwClient;

    @FXML
    private TableColumn<PresupuestosDTO, String> tcJardin, tcUbicacion, tcRechazado, tcSolicitud;

    @FXML
    private void initialize() throws IOException {
        implRetroFit = new ImplRetroFit();
        tcUbicacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUbicacion()));

        tcJardin.setCellValueFactory(cellData -> {
            String clienteId = String.valueOf(cellData.getValue().getIdJardin());
            JardinesDTO usuarioDTO = null;
            try {
                usuarioDTO = implRetroFit.getJardine(Integer.parseInt(clienteId));
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo obtener el usuario con ID: " + clienteId);
            }
            if (usuarioDTO != null) {
                return new SimpleStringProperty(usuarioDTO.getLocalizacion());
            } else {
                return new SimpleStringProperty("Desconocido");
            }
        });

        tcSolicitud.setCellValueFactory(cellData -> {
            String clienteId = String.valueOf(cellData.getValue().getIdSolicitud());
            SolicitudDTO solicitudDTO = null;
            try {
                solicitudDTO = implRetroFit.getSolicitud(Integer.parseInt(clienteId));
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo obtener la Solicitud con ID: " + clienteId);
            } catch (Exception e){

            }
            if (solicitudDTO != null) {
                UsuarioDTO usuarioDTO = null;
                try {
                    usuarioDTO = implRetroFit.getUsuario(solicitudDTO.getIdUsuario());
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "No se pudo obtener la Solicitud con ID: " + clienteId);
                }
                if (usuarioDTO != null) {
                    return new SimpleStringProperty(usuarioDTO.getNombre()+" "+usuarioDTO.getApellidos());
                } else {
                    return new SimpleStringProperty("Desconocido");
                }
            } else {
                return new SimpleStringProperty("Desconocido");
            }
        });

        tcRechazado.setCellValueFactory(cellData -> {
            String clienteId = String.valueOf(cellData.getValue().getRechazado());
            if(clienteId.equalsIgnoreCase("true")){
                return new SimpleStringProperty("si");
            } else {
                return new SimpleStringProperty("No");
            }
        });

        try {
            List<PresupuestosDTO> jardinDTOList = implRetroFit.getPresupuestos();

            for (PresupuestosDTO jardinDTO : jardinDTOList) {
                tvwClient.getItems().add(jardinDTO);
            }

            tvwClient.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo obtener la lista de jardines");
        }
    }
    public void goToCreate(ActionEvent event) {
    }

    public void goToUpdate(ActionEvent event) {
    }

    @FXML
    public void goToMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            MainController secondController = loader.getController();

            // Acceso al stage actual
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
