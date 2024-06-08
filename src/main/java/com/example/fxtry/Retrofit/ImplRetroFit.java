package com.example.fxtry.Retrofit;

import com.example.fxtry.Controller.PresupuestosController;
import com.example.fxtry.Model.*;
import com.example.fxtry.iService.iImagenService;
import com.example.fxtry.iService.iJardinesService;
import com.example.fxtry.iService.iPresupuestoDTO;
import com.example.fxtry.iService.iUsuarioService;
import com.example.fxtry.iService.iSolicitudeService;
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

    public ImplRetroFit() {
        this.jardinesService = RetrofitClient.getInstanceRetrofit().create(iJardinesService.class);
        this.usuarioService = RetrofitClient.getInstanceRetrofit().create(iUsuarioService.class);
        this.iImageneService = RetrofitClient.getInstanceRetrofit().create(iImagenService.class);
        this.iPresupuestoDTO = RetrofitClient.getInstanceRetrofit().create(iPresupuestoDTO.class);
        this.iSolicitudeService = RetrofitClient.getInstanceRetrofit().create(iSolicitudeService.class);
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
}
