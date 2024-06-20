package com.example.fxtry.iService;

import com.example.fxtry.Model.FacturaDTO;
import com.example.fxtry.Model.FileUpload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface iFacturaService {
    @GET("facturas")
    Call<List<FacturaDTO>> getFacturas();

    @POST("facturas/upload")
    Call<Void> uploadImage(@Body FileUpload imagenUploadDto);
}
