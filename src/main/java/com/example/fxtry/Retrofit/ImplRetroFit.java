package com.example.fxtry.Retrofit;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.UsuarioDTO;
import com.example.fxtry.iService.iUsuarioService;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

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
}
