package com.example.fxtry.iService;

import com.example.fxtry.Model.PresupuestosDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface iPresupuestoDTO {
    @GET("presupuestos")
    Call<List<PresupuestosDTO>> getPresupuestos();
}
