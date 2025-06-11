package com.example.fxtry.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private String fechaCreacion;
    private Long tamaño;
    private String usuario;
    private String categoria;
    private String contenidoBase64;
}