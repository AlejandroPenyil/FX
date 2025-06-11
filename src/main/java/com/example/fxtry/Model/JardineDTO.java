package com.example.fxtry.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JardineDTO {
    private Integer id;
    private String localizacion;
    private Integer tamaño;
    private Integer idCliente;
}
