package com.example.fxtry.Controller;

import com.example.fxtry.Controller.Create.JardinCreateController;
import com.example.fxtry.Model.*;
import com.example.fxtry.Retrofit.ImplRetroFit;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class JardinesController {
    ImplRetroFit implRetroFit;

    @FXML
    private TableView<JardinesDTO> tvwClient;

    @FXML
    private TableColumn<JardinesDTO, String> tcLocalizacion, tcTamaño, tcCliente;

    @FXML
    private TextField txtComentario;

    @FXML
    private void initialize() throws IOException {
        implRetroFit = new ImplRetroFit();
        tcLocalizacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalizacion()));
        tcTamaño.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTamaño().toString()));
        tcCliente.setCellValueFactory(cellData -> {
            String clienteId = String.valueOf(cellData.getValue().getIdCliente());
            UsuarioDTO usuarioDTO = null;
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
            List<JardinesDTO> jardinDTOList = implRetroFit.getJardines();

            for (JardinesDTO jardinDTO : jardinDTOList) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/jardin/jardin-create-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);
            // Acceso al controlador de la segunda escena, si es necesario
            JardinCreateController secondController = loader.getController();

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

    public void subir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Busca la imagen");

        // Agregar filtros de extensión si es necesario
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images Files", "*.png", ".jpg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        ImagenDTO imagenDTO = new ImagenDTO();
        JardinesDTO selectedJardin = tvwClient.getSelectionModel().getSelectedItem();

        if (selectedJardin != null) {
            // Mostrar el FileChooser y capturar el archivo seleccionado
            File selectedFile = fileChooser.showOpenDialog(currentStage);
            if (selectedFile != null) {
                try {
                    ImagenUploadDto imagenUploadDto = new ImagenUploadDto();
                    // Leer el contenido del archivo en un arreglo de bytes
                    byte[] fileContent = Files.readAllBytes(selectedFile.toPath());

                    imagenDTO.setIdJardin(selectedJardin.getId());
                    imagenDTO.setIdUsuario(selectedJardin.getIdCliente());
                    imagenDTO.setComentario(txtComentario.getText());
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    imagenUploadDto.setContent(encodedString);
                    imagenUploadDto.setFileName(selectedFile.getName());
                    imagenUploadDto.setFileType(Files.probeContentType(selectedFile.toPath()));
                    imagenUploadDto.setImagenDTO(imagenDTO);

                    System.out.println(imagenUploadDto.getFileName());

                    implRetroFit.uploadImagenes(imagenUploadDto);

                    // Aquí puedes guardar la cadena codificada en Base64 en algún lugar si lo necesitas
                } catch (IOException e) {
                    e.printStackTrace();
                    AlertController.showAlert("File Error", "Could not read the file: " + e.getMessage());
                }
            }
        }
    }

    public void subirPresupuesto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Busca el presupuesto");

        // Agregar filtros de extensión si es necesario
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        JardinesDTO selectedJardin = tvwClient.getSelectionModel().getSelectedItem();

        if (selectedJardin != null) {
            // Mostrar el FileChooser y capturar el archivo seleccionado
            File selectedFile = fileChooser.showOpenDialog(currentStage);
            if (selectedFile != null) {
                try {
                    FileUpload imagenUploadDto = new FileUpload();
                    // Leer el contenido del archivo en un arreglo de bytes
                    byte[] fileContent = Files.readAllBytes(selectedFile.toPath());

                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    imagenUploadDto.setContent(encodedString);
                    imagenUploadDto.setFileName(selectedFile.getName());
                    imagenUploadDto.setFileType(Files.probeContentType(selectedFile.toPath()));
                    imagenUploadDto.setId(selectedJardin.getId());

                    System.out.println(imagenUploadDto.getId());

                    implRetroFit.uploadPresupuesto(imagenUploadDto);

                    // Aquí puedes guardar la cadena codificada en Base64 en algún lugar si lo necesitas
                } catch (IOException e) {
                    e.printStackTrace();
                    AlertController.showAlert("File Error", "Could not read the file: " + e.getMessage());
                }
            }
        }
    }
}
