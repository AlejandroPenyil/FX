package com.example.fxtry.iService;

import com.example.fxtry.Model.LogginRequest;
import com.example.fxtry.Model.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface iUsuarioService {
    @POST("/usuarios/login")
    Call<UsuarioDTO> usuarioLogin(@Body LogginRequest loginRequest);
}
