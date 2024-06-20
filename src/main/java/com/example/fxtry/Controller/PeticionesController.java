package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
import com.example.fxtry.Model.JardinesDTO;
import com.example.fxtry.Model.SolicitudDTO;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PeticionesController {
    public static SolicitudDTO updatable = new SolicitudDTO();
    ImplRetroFit implRetroFit;
    @FXML
    private TableView<SolicitudDTO> tvwClient;

    @FXML
    private TableColumn<SolicitudDTO, String> tcName, tcFecha, tcAtendida, tcDescripcion;

    @FXML
    private void initialize(){
        implRetroFit = new ImplRetroFit();
        tcFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaSolicitud()));
        tcDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));

        tcAtendida.setCellValueFactory(cellData -> {
            String clienteId = String.valueOf(cellData.getValue().isAtendida());
            if(clienteId.equalsIgnoreCase("true")){
                return new SimpleStringProperty("si");
            } else {
                return new SimpleStringProperty("No");
            }
        });

        tcName.setCellValueFactory(cellData -> {
            String clienteId = String.valueOf(cellData.getValue().getIdUsuario());
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            try {
                usuarioDTO = implRetroFit.getUsuario(Integer.parseInt(clienteId));
            } catch (IOException e) {
                e.printStackTrace();
                AlertController.showAlert("Error", "No se pudo obtener el usuario con ID: " + clienteId);
            }
            if (usuarioDTO != null) {
                return new SimpleStringProperty(usuarioDTO.getNombre() + " " + usuarioDTO.getApellidos());
            } else {
                return new SimpleStringProperty("Desconocido");
            }
        });

        try {
            List<SolicitudDTO> jardinDTOList = implRetroFit.getSolicitudes();

            for (SolicitudDTO jardinDTO : jardinDTOList) {
                tvwClient.getItems().add(jardinDTO);
            }

            tvwClient.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error", "No se pudo obtener la lista de jardines");
        }
    }

    @FXML
    private void goToMain(ActionEvent event) {
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

    public void goToCreate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-create-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ClientCreateController secondController = loader.getController();

            // Acceso al stage actual
            MenuItem menuItem = (MenuItem) event.getSource();
            ContextMenu contextMenu = menuItem.getParentPopup();
            Scene scene = contextMenu.getOwnerNode().getScene();
            Stage currentStage = (Stage) scene.getWindow();
            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToUpdate(ActionEvent event) {
        try {
            SolicitudDTO selectedUsuarioDTO = tvwClient.getSelectionModel().getSelectedItem();

            updatable= selectedUsuarioDTO;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Client/client-update-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            ClientUpdateController secondController = loader.getController();

            // Acceso al stage actual
            MenuItem menuItem = (MenuItem) event.getSource();
            ContextMenu contextMenu = menuItem.getParentPopup();
            Scene scene = contextMenu.getOwnerNode().getScene();
            Stage currentStage = (Stage) scene.getWindow();

            secondScene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            // Mostrar la segunda escena en el stage actual
            currentStage.setScene(secondScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent event){
        SolicitudDTO selectedUsuarioDTO = tvwClient.getSelectionModel().getSelectedItem();

        //TODO metodo para softdelete, no va haber hard delete
    }
}
