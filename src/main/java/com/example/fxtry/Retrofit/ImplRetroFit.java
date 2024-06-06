package com.example.fxtry.Retrofit;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.iService.iUsuarioService;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class ImplRetroFit {
    private final iUsuarioService usuarioService;

    public ImplRetroFit() {
        this.usuarioService = RetrofitClient.getInstanceRetrofit().create(iUsuarioService.class);
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

    public List<UsuarioDTO> getUsuarios() throws IOException {
        Call<List<UsuarioDTO>> call = usuarioService.getUsuarios();
        Response<List<UsuarioDTO>> response = call.execute();
        return response.body();
    }

    public UsuarioDTO putUsuario(UsuarioDTO usuarioDTO) throws IOException {
        Call<UsuarioDTO> call = usuarioService.usuarioUpdate(usuarioDTO.getId(),usuarioDTO);
        Response<UsuarioDTO> response = call.execute();
        return response.body();
    }
}
