package com.example.fxtry.iService;

import com.example.fxtry.Model.FileUpload;
import com.example.fxtry.Model.PresupuestosDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface iPresupuestoDTO {
    @GET("presupuestos")
    Call<List<PresupuestosDTO>> getPresupuestos();

    @POST("presupuestos/upload")
    Call<Void> uploadImage(@Body FileUpload imagenUploadDto);
}
