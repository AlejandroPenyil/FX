package com.example.fxtry.iService;

import com.example.fxtry.Model.ImagenDTO;
import com.example.fxtry.Model.ImagenUploadDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface iImagenService {
    @POST("imagenes/upload")
    Call<Void> uploadImage(@Body ImagenUploadDto image);

    @GET("imagenes")
    Call<List<ImagenDTO>> getAllImages();
}
