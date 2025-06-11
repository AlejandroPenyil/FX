package com.example.fxtry.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String fechaAdquisicion;
    private Double valor;
    private String estado;
    private String ubicacion;
}