package com.example.fxtry.Model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class DocumentDto {
    private Integer id;
    private Boolean isFolder;
    private String name;
    private ZonedDateTime fecha;
    private Integer idCliente;
    private Integer idParent;

}
