package com.alura.literalura.model;

import java.util.List;

public record ApiBooks(int count, String next, String previous, List<DatosLibro> results) { }
