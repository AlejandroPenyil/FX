package com.example.fxtry.iService;

import com.example.fxtry.Model.FileUpload;
import com.example.fxtry.Model.PresupuestosDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface iPresupuestoDTO {
    @GET("presupuestos")
    Call<List<PresupuestosDTO>> getPresupuestos();

    @GET("presupuestos/{id}")
    Call<PresupuestosDTO> getPresupuesto(@Path("id") Integer id);

    @POST("presupuestos")
    Call<PresupuestosDTO> createPresupuesto(@Body PresupuestosDTO presupuestoDTO);

    @PUT("presupuestos/{id}")
    Call<PresupuestosDTO> updatePresupuesto(@Path("id") Integer id, @Body PresupuestosDTO presupuestoDTO);

    @DELETE("presupuestos/{id}")
    Call<Void> deletePresupuesto(@Path("id") Integer id);

    @POST("presupuestos/upload")
    Call<Void> uploadImage(@Body FileUpload imagenUploadDto);
}
