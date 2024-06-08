package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
import com.example.fxtry.Model.ImagenDTO;
import com.example.fxtry.Model.JardinesDTO;
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

import static com.example.fxtry.Controller.AlertController.showAlert;

public class ImagenesController {
    public static ImagenDTO updatable = new ImagenDTO();
    ImplRetroFit implRetroFit;

    @FXML
    private TableView<ImagenDTO> tvwClient;

    @FXML
    private TableColumn<ImagenDTO, String> tcFecha,tcUrl,tcCliente,tcJardin,tcComentario;

    @FXML
    private void initialize() throws IOException {
        implRetroFit = new ImplRetroFit();
        tcUrl.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUbicacion()));
        tcFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha()));
        tcComentario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComentario()));

        tcCliente.setCellValueFactory(cellData -> {
            String clienteId = String.valueOf(cellData.getValue().getIdUsuario());
            UsuarioDTO usuarioDTO = null;
            try {
                usuarioDTO = implRetroFit.getUsuario(Integer.parseInt(clienteId));
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo obtener el usuario con ID: " + clienteId);
            }
            if (usuarioDTO != null) {
                return new SimpleStringProperty(usuarioDTO.getNombre() + " " + usuarioDTO.getApellidos());
            } else {
                return new SimpleStringProperty("Desconocido");
            }
        });

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

        List<ImagenDTO> imagenDTOList =  implRetroFit.getImagenes();
        for(ImagenDTO imagenDTO : imagenDTOList){
            tvwClient.getItems().add(imagenDTO);
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
            ImagenDTO selectedUsuarioDTO = tvwClient.getSelectionModel().getSelectedItem();

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
        ImagenDTO selectedUsuarioDTO = tvwClient.getSelectionModel().getSelectedItem();

        //TODO metodo para softdelete, no va haber hard delete
    }
}
