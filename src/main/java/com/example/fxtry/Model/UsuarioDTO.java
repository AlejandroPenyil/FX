package com.example.fxtry.Model;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String contraseña;
    private String correo;
    private String dni;
    private String rol;
    private String telefono;
    private String direccion;
}
