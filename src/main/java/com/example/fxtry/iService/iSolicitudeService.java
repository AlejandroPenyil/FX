package com.example.fxtry.iService;

import com.example.fxtry.Model.SolicitudDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface iSolicitudeService {
    @GET("solicitudes/{id}")
    Call<SolicitudDTO> getSolicitud(@Path("id") Integer id);

    @GET("solicitudes")
    Call<List<SolicitudDTO>> getSolicitudes();

    @POST("solicitudes")
    Call<SolicitudDTO> createSolicitud(@Body SolicitudDTO solicitudDTO);

    @PUT("solicitudes/{id}")
    Call<SolicitudDTO> updateSolicitud(@Path("id") Integer id, @Body SolicitudDTO solicitudDTO);

    @DELETE("solicitudes/{id}")
    Call<Void> deleteSolicitud(@Path("id") Integer id);
}
