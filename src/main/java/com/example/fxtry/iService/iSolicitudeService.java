package com.example.fxtry.iService;

import com.example.fxtry.Model.SolicitudDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface iSolicitudeService {
    @GET("solicitudes/{id}")
    Call<SolicitudDTO> getSolicitud(@Path("id") Integer id);

    @GET("solicitudes")
    Call<List<SolicitudDTO>> getSolicitudes();
}
