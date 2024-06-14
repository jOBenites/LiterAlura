package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(Long id,
                         String title,
                         List<DatosAutor> authors,
                         List<String> languages,
                         Long download_count) {
}
