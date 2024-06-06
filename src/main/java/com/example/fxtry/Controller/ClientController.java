package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
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

public class ClientController {
    public static UsuarioDTO updatable = new UsuarioDTO();

    //TODO crear variable para realizar el filtrado

    ImplRetroFit implRetroFit;

    @FXML
    private TableView<UsuarioDTO> tvwClient;

    @FXML
    private TableColumn<UsuarioDTO, String> tcName, tcPassword, tcUsuario, tcId, tcCorreo, tcApellido, tcDni, tcTelefono, tcDireccion, tcRol;

    @FXML
    private void initialize() throws IOException {
        implRetroFit = new ImplRetroFit();
        tcName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcPassword.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContraseña()));
        tcUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        tcApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellidos()));
        tcCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreo()));
        tcDireccion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion()));
        tcRol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRol()));
        tcDni.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDni()));
        tcId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        tcTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
//        tvwClient.getItems().add(admin);
        List<UsuarioDTO> usuarioDTOList = implRetroFit.getUsuarios();

        for (UsuarioDTO usuarioDTO : usuarioDTOList){
            tvwClient.getItems().add(usuarioDTO);
        }

        tvwClient.refresh();
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
            UsuarioDTO selectedUsuarioDTO = tvwClient.getSelectionModel().getSelectedItem();

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

    public void delete(ActionEvent event) throws IOException {
        UsuarioDTO selectedUsuarioDTO = tvwClient.getSelectionModel().getSelectedItem();

        implRetroFit.deleteUsuario(selectedUsuarioDTO);

        tvwClient.getItems().remove(selectedUsuarioDTO);

        // Actualiza la tabla para reflejar los cambios
        tvwClient.refresh();
    }

}

