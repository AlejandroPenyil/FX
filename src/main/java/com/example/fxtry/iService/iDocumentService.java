package com.example.fxtry.iService;

import com.example.fxtry.Model.DocumentDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface iDocumentService {
    @GET("documents")
    Call<List<DocumentDto>> getAllDocuments();

    @GET("documents/{id}")
    Call<DocumentDto> getDocumentById(@Path("id") Integer id);

    @POST("documents")
    Call<DocumentDto> createDocument(@Body DocumentDto documentDto);

    @PUT("documents/{id}")
    Call<DocumentDto> updateDocument(@Path("id") Integer id, @Body DocumentDto documentDto);

    @DELETE("documents/{id}")
    Call<Void> deleteDocument(@Path("id") Integer id);
}