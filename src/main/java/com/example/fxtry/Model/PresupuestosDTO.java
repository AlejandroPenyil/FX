package com.example.fxtry.Model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PresupuestosDTO {
    private Integer id;
    private String fechalEnvio;
    private String fechaAceptado;
    private Boolean rechazado;
    private Integer idJardin;
    private Integer idSolicitud;
    private String ubicacion;
}
