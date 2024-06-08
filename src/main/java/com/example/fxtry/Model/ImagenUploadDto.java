package com.example.fxtry.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenUploadDto {
    private String fileName;
    private String fileType;
    private String content;
    private ImagenDTO imagenDTO;
}
