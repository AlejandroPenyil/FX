package com.example.fxtry.iService;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface iUsuarioService {
    @POST("/usuarios/login")
    Call<UsuarioDTO> usuarioLogin(@Body LogginRequest loginRequest);

    @GET("/usuarios")
    Call<List<UsuarioDTO>> getUsuarios();
}