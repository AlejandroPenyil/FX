package com.example.fxtry.Retrofit;

import com.example.fxtry.Controller.PresupuestosController;
import com.example.fxtry.Model.*;
import com.example.fxtry.iService.*;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static com.example.fxtry.Controller.LoginController.admin;

public class ImplRetroFit {
    private final iUsuarioService usuarioService;
    private final iJardinesService jardinesService;
    private final iImagenService iImageneService;
    private final iPresupuestoDTO iPresupuestoDTO;
    private final iSolicitudeService iSolicitudeService;
    private final iFacturaService iFacturaService;
    private final iDocumentService iDocumentService;
    private final iMensajeriaService iMensajeriaService;

    public ImplRetroFit() {
        this.jardinesService = RetrofitClient.getInstanceRetrofit().create(iJardinesService.class);
        this.usuarioService = RetrofitClient.getInstanceRetrofit().create(iUsuarioService.class);
        this.iImageneService = RetrofitClient.getInstanceRetrofit().create(iImagenService.class);
        this.iPresupuestoDTO = RetrofitClient.getInstanceRetrofit().create(iPresupuestoDTO.class);
        this.iSolicitudeService = RetrofitClient.getInstanceRetrofit().create(iSolicitudeService.class);
        this.iFacturaService = RetrofitClient.getInstanceRetrofit().create(iFacturaService.class);
        this.iDocumentService = RetrofitClient.getInstanceRetrofit().create(iDocumentService.class);
        this.iMensajeriaService = RetrofitClient.getInstanceRetrofit().create(iMensajeriaService.class);
    }

    public UsuarioDTO usuarioLogin(LogginRequest loginRequest) throws IOException {
        Call<UsuarioDTO> call = usuarioService.usuarioLogin(loginRequest);
        Response<UsuarioDTO> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            return null;
        }
    }

    public interface LoginCallback {
        void onSuccess(UsuarioDTO usuario);
        void onError(String errorMessage);
    }

    public void usuarioLoginAsync(LogginRequest loginRequest, LoginCallback callback) {
        Call<UsuarioDTO> call = usuarioService.usuarioLogin(loginRequest);
        call.enqueue(new retrofit2.Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error de autenticación: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }


    //usuarios
    public UsuarioDTO usarioCreate(UsuarioDTO usuarioDTO) throws IOException {
        Call<UsuarioDTO> call = usuarioService.usuarioCreate(usuarioDTO);
        Response<UsuarioDTO> response = call.execute();
        return response.body();
    }

    public List<UsuarioDTO> getUsuarios() throws IOException {
        Call<List<UsuarioDTO>> call = usuarioService.getUsuarios();
        Response<List<UsuarioDTO>> response = call.execute();
        return response.body();
    }

    public UsuarioDTO getUsuario(int id) throws IOException {
        Call<UsuarioDTO> call = usuarioService.getUsuario(id);
        Response<UsuarioDTO> response = call.execute();
        return response.body();
    }

    public UsuarioDTO putUsuario(UsuarioDTO usuarioDTO) throws IOException {
        Call<UsuarioDTO> call = usuarioService.usuarioUpdate(usuarioDTO.getId(),usuarioDTO);
        Response<UsuarioDTO> response = call.execute();
        return response.body();
    }

    public void deleteUsuario(UsuarioDTO usuarioDTO) throws IOException {
        Call<Void> call = usuarioService.usuarioDelete(usuarioDTO.getId());
        call.execute();
    }


    //Jardines
    public List<JardinesDTO> getJardines() throws IOException {
        Call<List<JardinesDTO>> call = jardinesService.getJardines();
        Response<List<JardinesDTO>> response = call.execute();
        return response.body();
    }

    public JardinesDTO getJardine(Integer id) throws IOException {
        Call<JardinesDTO> call = jardinesService.getJardine(id);
        Response<JardinesDTO> response = call.execute();
        return response.body();
    }


    // Imagenes
    public Void uploadImagenes(ImagenUploadDto imagenUploadDto) throws IOException {
        Call<Void> call = iImageneService.uploadImage(imagenUploadDto);
        call.execute();
        return null;
    }

    public List<ImagenDTO> getImagenes() throws IOException {
        Call<List<ImagenDTO>> call = iImageneService.getAllImages();
        Response<List<ImagenDTO>> response = call.execute();
        return response.body();
    }


    // Presupuestos
    public List<PresupuestosDTO> getPresupuestos() throws IOException {
        Call<List<PresupuestosDTO>> call = iPresupuestoDTO.getPresupuestos();
        Response<List<PresupuestosDTO>> response = call.execute();
        return response.body();
    }

    public PresupuestosDTO getPresupuesto(Integer id) throws IOException {
        Call<PresupuestosDTO> call = iPresupuestoDTO.getPresupuesto(id);
        Response<PresupuestosDTO> response = call.execute();
        return response.body();
    }

    public PresupuestosDTO createPresupuesto(PresupuestosDTO presupuestoDTO) throws IOException {
        Call<PresupuestosDTO> call = iPresupuestoDTO.createPresupuesto(presupuestoDTO);
        Response<PresupuestosDTO> response = call.execute();
        return response.body();
    }

    public PresupuestosDTO updatePresupuesto(Integer id, PresupuestosDTO presupuestoDTO) throws IOException {
        Call<PresupuestosDTO> call = iPresupuestoDTO.updatePresupuesto(id, presupuestoDTO);
        Response<PresupuestosDTO> response = call.execute();
        return response.body();
    }

    public void deletePresupuesto(Integer id) throws IOException {
        Call<Void> call = iPresupuestoDTO.deletePresupuesto(id);
        call.execute();
    }


    // Solicitudes
    public SolicitudDTO getSolicitud(Integer id) throws IOException {
        Call<SolicitudDTO> call = iSolicitudeService.getSolicitud(id);
        Response<SolicitudDTO> response = call.execute();
        return response.body();
    }

    public List<SolicitudDTO> getSolicitudes() throws IOException {
        Call<List<SolicitudDTO>> call = iSolicitudeService.getSolicitudes();
        Response<List<SolicitudDTO>> response = call.execute();
        return response.body();
    }

    public SolicitudDTO createSolicitud(SolicitudDTO solicitudDTO) throws IOException {
        Call<SolicitudDTO> call = iSolicitudeService.createSolicitud(solicitudDTO);
        Response<SolicitudDTO> response = call.execute();
        return response.body();
    }

    public SolicitudDTO updateSolicitud(Integer id, SolicitudDTO solicitudDTO) throws IOException {
        Call<SolicitudDTO> call = iSolicitudeService.updateSolicitud(id, solicitudDTO);
        Response<SolicitudDTO> response = call.execute();
        return response.body();
    }

    public void deleteSolicitud(Integer id) throws IOException {
        Call<Void> call = iSolicitudeService.deleteSolicitud(id);
        call.execute();
    }


    // Facturas
    public List<FacturaDTO> getFacturas() throws IOException {
        Call<List<FacturaDTO>> call = iFacturaService.getFacturas();
        Response<List<FacturaDTO>> response = call.execute();
        return response.body();
    }

    public FacturaDTO getFactura(Integer id) throws IOException {
        Call<FacturaDTO> call = iFacturaService.getFactura(id);
        Response<FacturaDTO> response = call.execute();
        return response.body();
    }

    public FacturaDTO createFactura(FacturaDTO facturaDTO) throws IOException {
        Call<FacturaDTO> call = iFacturaService.createFactura(facturaDTO);
        Response<FacturaDTO> response = call.execute();
        return response.body();
    }

    public FacturaDTO updateFactura(Integer id, FacturaDTO facturaDTO) throws IOException {
        Call<FacturaDTO> call = iFacturaService.updateFactura(id, facturaDTO);
        Response<FacturaDTO> response = call.execute();
        return response.body();
    }

    public void deleteFactura(Integer id) throws IOException {
        Call<Void> call = iFacturaService.deleteFactura(id);
        call.execute();
    }

    public void uploadFacturas(FileUpload imagenUploadDto) throws IOException {
        Call<Void> call = iFacturaService.uploadImage(imagenUploadDto);
        call.execute();
    }

    public void uploadPresupuesto(FileUpload imagenUploadDto) throws IOException {
        Call<Void> call = iPresupuestoDTO.uploadImage(imagenUploadDto);
        call.execute();
    }

    // Reset Password
    public boolean resetPassword(PasswordResetRequest resetRequest) throws IOException {
        Call<Void> call = usuarioService.resetPassword(resetRequest);
        Response<Void> response = call.execute();
        return response.isSuccessful();
    }

    // Documents
    public List<DocumentDto> getAllDocuments() throws IOException {
        Call<List<DocumentDto>> call = iDocumentService.getAllDocuments();
        Response<List<DocumentDto>> response = call.execute();
        return response.body();
    }

    public DocumentDto getDocumentById(Integer id) throws IOException {
        Call<DocumentDto> call = iDocumentService.getDocumentById(id);
        Response<DocumentDto> response = call.execute();
        return response.body();
    }

    public DocumentDto createDocument(DocumentDto documentDto) throws IOException {
        Call<DocumentDto> call = iDocumentService.createDocument(documentDto);
        Response<DocumentDto> response = call.execute();
        return response.body();
    }

    public DocumentDto updateDocument(Integer id, DocumentDto documentDto) throws IOException {
        Call<DocumentDto> call = iDocumentService.updateDocument(id, documentDto);
        Response<DocumentDto> response = call.execute();
        return response.body();
    }

    public void deleteDocument(Integer id) throws IOException {
        Call<Void> call = iDocumentService.deleteDocument(id);
        call.execute();
    }

    // Mensajeria
    public List<ConversacionDTO> getAllConversaciones() throws IOException {
        Call<List<ConversacionDTO>> call = iMensajeriaService.getAllConversaciones();
        Response<List<ConversacionDTO>> response = call.execute();
        return response.body();
    }

    public ConversacionDTO getConversacionById(Integer id) throws IOException {
        Call<ConversacionDTO> call = iMensajeriaService.getConversacionById(id);
        Response<ConversacionDTO> response = call.execute();
        return response.body();
    }

    public ConversacionDTO getConversacionByUsuario(Integer usuarioId) throws IOException {
        Call<ConversacionDTO> call = iMensajeriaService.getConversacionByUsuario(usuarioId);
        Response<ConversacionDTO> response = call.execute();
        return response.body();
    }

    public MensajeDTO enviarMensaje(MensajeDTO mensajeDTO) throws IOException {
        Call<MensajeDTO> call = iMensajeriaService.enviarMensaje(mensajeDTO);
        Response<MensajeDTO> response = call.execute();
        return response.body();
    }

    public void marcarComoLeidos(Integer conversacionId, Integer usuarioId) throws IOException {
        Call<Void> call = iMensajeriaService.marcarComoLeidos(conversacionId, usuarioId);
        call.execute();
    }
}
