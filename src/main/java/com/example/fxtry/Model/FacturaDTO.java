package com.example.fxtry.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaDTO {
    private Integer id;
    private String fecha;
    private String ubicacion;
    private Integer idCliente;
}