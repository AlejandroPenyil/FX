package com.example.fxtry.iService;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface iUsuarioService {
    @POST("/usuarios/login")
    Call<UsuarioDTO> usuarioLogin(@Body LogginRequest loginRequest);

    @GET("/usuarios")
    Call<List<UsuarioDTO>> getUsuarios();

    @PUT("/usuarios/{id}")
    Call<UsuarioDTO> usuarioUpdate(@Path("id")Integer id ,@Body UsuarioDTO loginRequest);

    @DELETE("/usuarios/{id}")
    Call<Void> usuarioDelete(@Path("id") Integer id);
}
