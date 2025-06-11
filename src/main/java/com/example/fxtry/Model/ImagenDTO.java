package com.example.fxtry.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenDTO {
    private Integer id;
    private String fecha;
    private String ubicacion;
    private Integer idJardin;
    private Integer idUsuario;
    private String comentario;
    private String imageDataBase64;
}
