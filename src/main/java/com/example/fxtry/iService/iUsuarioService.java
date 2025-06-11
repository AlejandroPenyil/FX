package com.example.fxtry.iService;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.PasswordResetRequest;
import com.example.fxtry.Model.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface iUsuarioService {
    @POST("/usuarios/login")
    Call<UsuarioDTO> usuarioLogin(@Body LogginRequest loginRequest);

    @POST("/usuarios")
    Call<UsuarioDTO> usuarioCreate(@Body UsuarioDTO usuarioDTO);

    @GET("/usuarios")
    Call<List<UsuarioDTO>> getUsuarios();

    @GET("/usuarios/{id}")
    Call<UsuarioDTO> getUsuario(@Path("id") Integer id);

    @PUT("/usuarios/{id}")
    Call<UsuarioDTO> usuarioUpdate(@Path("id")Integer id ,@Body UsuarioDTO loginRequest);

    @DELETE("/usuarios/{id}")
    Call<Void> usuarioDelete(@Path("id") Integer id);

    @POST("/usuarios/reset-password")
    Call<Void> resetPassword(@Body PasswordResetRequest resetRequest);
}
