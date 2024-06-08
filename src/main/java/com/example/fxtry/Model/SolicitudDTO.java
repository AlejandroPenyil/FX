package com.example.fxtry.Model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SolicitudDTO {
    private Integer id;
    private String fechaSolicitud;
    private Integer idUsuario;
    private String descripcion;
    private boolean atendida;
}
