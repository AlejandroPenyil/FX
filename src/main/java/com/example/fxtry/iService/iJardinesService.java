package com.example.fxtry.iService;

import com.example.fxtry.Model.JardinesDTO;
import javafx.fxml.FXML;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface iJardinesService {
    @GET("jardines")
    Call<List<JardinesDTO>> getJardines();

    @GET("jardines/{id}")
    Call<JardinesDTO> getJardine(@Path("id") Integer id);
}
