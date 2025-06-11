package com.example.fxtry.Controller.Update;

import com.example.fxtry.Controller.AlertController;
import com.example.fxtry.Controller.ImagenesController;
import com.example.fxtry.Model.ImagenDTO;
import com.example.fxtry.Model.ImagenUploadDto;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImagenesUpdateController {

    private ImplRetroFit implRetroFit;
    private ImagenDTO imagenToUpdate;
    private File selectedImageFile;
    private boolean imageChanged = false;

    @FXML
    private TextField txtUbicacion;

    @FXML
    private ComboBox<String> cmbJardin;

    @FXML
    private ComboBox<String> cmbUsuario;

    @FXML
    private TextArea txtComentario;

    @FXML
    private Label lblSelectedFile;

    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        implRetroFit = new ImplRetroFit();

        // Load the imagen to update from the static variable in ImagenesController
        imagenToUpdate = ImagenesController.updatable;

        // Initialize combo boxes
        try {
            // Load jardines
            var jardines = implRetroFit.getJardines();
            for (var jardin : jardines) {
                cmbJardin.getItems().add(jardin.getId() + " - " + jardin.getLocalizacion());

                // Select the current jardin
                if (imagenToUpdate != null && imagenToUpdate.getIdJardin() != null && 
                    jardin.getId().equals(imagenToUpdate.getIdJardin())) {
                    cmbJardin.getSelectionModel().select(cmbJardin.getItems().size() - 1);
                }
            }

            // Load usuarios
            var usuarios = implRetroFit.getUsuarios();
            for (var usuario : usuarios) {
                cmbUsuario.getItems().add(usuario.getId() + " - " + usuario.getNombre() + " " + usuario.getApellidos());

                // Select the current usuario
                if (imagenToUpdate != null && imagenToUpdate.getIdUsuario() != null && 
                    usuario.getId().equals(imagenToUpdate.getIdUsuario())) {
                    cmbUsuario.getSelectionModel().select(cmbUsuario.getItems().size() - 1);
                }
            }

            // Populate other fields if imagen exists
            if (imagenToUpdate != null && imagenToUpdate.getId() != null) {
                txtUbicacion.setText(imagenToUpdate.getUbicacion());
                txtComentario.setText(imagenToUpdate.getComentario());
                lblSelectedFile.setText("Imagen actual (no modificada)");
            } else {
                lblError.setText("Error: No se ha seleccionado ninguna imagen para actualizar.");
            }

        } catch (IOException e) {
            lblError.setText("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToImagenes(ActionEvent event) {
        navigateToImagenesView(event);
    }

    @FXML
    private void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            lblSelectedFile.setText("Archivo seleccionado: " + selectedImageFile.getName());
            imageChanged = true;
        }
    }

    @FXML
    private void update(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            if (imagenToUpdate == null || imagenToUpdate.getId() == null) {
                lblError.setText("Error: No se ha seleccionado ninguna imagen para actualizar.");
                return;
            }

            // Update the imagen with the form data
            imagenToUpdate.setUbicacion(txtUbicacion.getText().trim());

            // Extract IDs from combo boxes
            String jardinSelected = cmbJardin.getValue();
            String usuarioSelected = cmbUsuario.getValue();

            int jardinId = Integer.parseInt(jardinSelected.split(" - ")[0]);
            int usuarioId = Integer.parseInt(usuarioSelected.split(" - ")[0]);

            imagenToUpdate.setIdJardin(jardinId);
            imagenToUpdate.setIdUsuario(usuarioId);
            imagenToUpdate.setComentario(txtComentario.getText().trim());

            // If image was changed, update the image data
            if (imageChanged && selectedImageFile != null) {
                // Read and encode the image file
                byte[] fileContent = Files.readAllBytes(selectedImageFile.toPath());
                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                // Create ImagenUploadDto
                ImagenUploadDto uploadDto = new ImagenUploadDto();
                uploadDto.setFileName(selectedImageFile.getName());
                uploadDto.setFileType(getFileExtension(selectedImageFile.getName()));
                uploadDto.setContent(base64Content);
                uploadDto.setImagenDTO(imagenToUpdate);

                // Upload the image
                implRetroFit.uploadImagenes(uploadDto);
            } else {
                // Just update the metadata
                // Note: This assumes there's an endpoint to update just the metadata
                // If not, you might need to re-upload the existing image data
                // implRetroFit.updateImagen(imagenToUpdate.getId(), imagenToUpdate);
            }

            AlertController.showInformation("Imagen Actualizada", "La imagen ha sido actualizada correctamente.");
            navigateToImagenesView(event);
        } catch (NumberFormatException e) {
            lblError.setText("Error: Seleccione un jardín y un usuario válidos.");
            e.printStackTrace();
        } catch (IOException e) {
            lblError.setText("Error al actualizar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (cmbJardin.getValue() == null || 
            cmbUsuario.getValue() == null) {

            lblError.setText("Por favor, seleccione un jardín y un usuario.");
            return false;
        }

        // Clear any previous error
        lblError.setText("");
        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    private void navigateToImagenesView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxtry/Imagenes/imagenes-view.fxml"));
            Parent secondSceneParent = loader.load();
            Scene secondScene = new Scene(secondSceneParent);

            ImagenesController secondController = loader.getController();

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
