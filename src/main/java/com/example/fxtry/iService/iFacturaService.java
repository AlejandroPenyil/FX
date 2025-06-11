package com.example.fxtry.iService;

import com.example.fxtry.Model.FacturaDTO;
import com.example.fxtry.Model.FileUpload;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface iFacturaService {
    @GET("facturas")
    Call<List<FacturaDTO>> getFacturas();

    @GET("facturas/{id}")
    Call<FacturaDTO> getFactura(@Path("id") Integer id);

    @POST("facturas")
    Call<FacturaDTO> createFactura(@Body FacturaDTO facturaDTO);

    @PUT("facturas/{id}")
    Call<FacturaDTO> updateFactura(@Path("id") Integer id, @Body FacturaDTO facturaDTO);

    @DELETE("facturas/{id}")
    Call<Void> deleteFactura(@Path("id") Integer id);

    @POST("facturas/upload")
    Call<Void> uploadImage(@Body FileUpload imagenUploadDto);
}
