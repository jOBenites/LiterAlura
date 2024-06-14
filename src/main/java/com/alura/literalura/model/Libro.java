package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @Column
    private Long cantidadDescargas;
    @Enumerated(EnumType.STRING)
    private Idioma idiomas;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "libros_detalle",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autores;

    public Libro() {}

    public Libro(String titulo, Long cantidadDescargas, Idioma idiomas, List<Autor> autores) {
        this.titulo = titulo;
        this.cantidadDescargas = cantidadDescargas;
        this.idiomas = idiomas;
        this.autores = autores;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public Long getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Long cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

    public Idioma getLenguajes() {
        return idiomas;
    }

    public void setLenguajes(Idioma idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                ", titulo='" + titulo + '\'' +
                ", cantidadDescargas=" + cantidadDescargas +
                ", lenguajes='" + idiomas + '\'' +
                ", autores=" + autores +
                '}';
    }
}
