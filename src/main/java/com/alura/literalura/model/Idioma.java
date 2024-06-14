package com.alura.literalura.model;

public enum Idioma {

    ESPANIOL("es"),
    INGLES("en"),
    FRANCES("fr"),
    PORTUGUES("pt");
    private String idiomaApi;

    Idioma(String idiomaApi) {
        this.idiomaApi = idiomaApi;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaApi.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
