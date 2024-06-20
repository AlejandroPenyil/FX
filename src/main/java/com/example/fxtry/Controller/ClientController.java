package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.ClientCreateController;
import com.example.fxtry.Controller.Update.ClientUpdateController;
import com.example.fxtry.Model.*;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.application.Application;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class ClientController extends Application {
    public static UsuarioDTO updatable = new UsuarioDTO();

    ImplRetroFit implRetroFit;

    @FXML
    private TableView<UsuarioDTO> tvwClient;

    @FXML
    private TableColumn<UsuarioDTO, String> tcName, tcPassword, tcUsuario, tcId, tcCorreo, tcApellido, tcDni, tcTelefono, tcDireccion, tcRol;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/fxtry/main-view.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/fxtry/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void goToHep(ActionEvent event) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI("https://concrete-binder-4b1.notion.site/Clientes-de41359ba305459988a3876795bad890");
            desktop.browse(uri);
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error al abrir la URL: " + e.getMessage());
        }
    }

    public void subir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Busca la imagen");

        // Agregar filtros de extensión si es necesario
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        ImagenDTO imagenDTO = new ImagenDTO();
        UsuarioDTO selectedJardin = tvwClient.getSelectionModel().getSelectedItem();

        if (selectedJardin != null) {
            // Mostrar el FileChooser y capturar el archivo seleccionado
            File selectedFile = fileChooser.showOpenDialog(currentStage);
            if (selectedFile != null) {
                try {
                    FileUpload imagenUploadDto = new FileUpload();
                    // Leer el contenido del archivo en un arreglo de bytes
                    byte[] fileContent = Files.readAllBytes(selectedFile.toPath());

                    imagenDTO.setIdJardin(selectedJardin.getId());
                    imagenDTO.setIdUsuario(selectedJardin.getId());
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    imagenUploadDto.setContent(encodedString);
                    imagenUploadDto.setFileName(selectedFile.getName());
                    imagenUploadDto.setFileType(Files.probeContentType(selectedFile.toPath()));
                    imagenUploadDto.setId(imagenDTO.getId());

                    System.out.println(imagenUploadDto.getFileName());

                    implRetroFit.uploadFacturas(imagenUploadDto);

                    // Aquí puedes guardar la cadena codificada en Base64 en algún lugar si lo necesitas
                } catch (IOException e) {
                    e.printStackTrace();
                    AlertController.showAlert("File Error", "Could not read the file: " + e.getMessage());
                }
            }
        }
    }
}

