package com.alura.literalura;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private final Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public ConsoleApplication(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ---------------------------------------------
                    Elija la opción a través de su número:
                    1 - Buscar libro por título 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma           
                    0 - Salir
                    ----------------------------------------------
                    """;
            System.out.println(menu);
            try{
                opcion = teclado.nextInt();
            }catch(Exception e){
                System.out.println("Opción inválida");
            }
            teclado.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroByTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosByAnio();
                case 5 -> listarLibrosByIdioma();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroByTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos("?search=" + nombreLibro.replace(" ", "%20"));
        var result = convierteDatos.obtenerDatos(json, ApiBooks.class);
        System.out.println(result);
        if(result.results().size() > 0){
            var libroApi = result.results().get(0);
            var libroExist = getBookExist(libroApi.title());
            if(libroExist == null) {
                Libro libro = new Libro(libroApi.title(), libroApi.download_count(), Idioma.fromString(libroApi.languages().get(0)), getAutores(libroApi.authors()));
                libroRepository.save(libro);

                System.out.println("--------LIBRO--------\n"+
                        "Título: " + libro.getTitulo() +"\n"+
                        "Autor: " + libro.getAutores().get(0) +"\n"+
                        "Idioma: " + libro.getLenguajes() +"\n"+
                        "Cantidad descargas: " + libro.getCantidadDescargas() +"\n"+
                        "------------------------");
            } else {
                System.out.println("El libro ya se encuentra registrado.");
            }
        } else {
            System.out.println("No se encontraron resultados.");
        }
    }

    private void listarLibrosRegistrados(){
        var libros = libroRepository.findAll();
        libros.forEach(libro ->   System.out.println("--------LIBRO--------\n"+
                "Título: " + libro.getTitulo() +"\n"+
                "Autor: " + libro.getAutores().get(0) +"\n"+
                "Idioma: " + libro.getLenguajes() +"\n"+
                "Cantidad descargas: " + libro.getCantidadDescargas() +"\n"+
                "------------------------"));
    }

    private void listarAutoresRegistrados(){
        var autores = autorRepository.findAll();
        autores.forEach(autor ->   System.out.println("--------AUTOR--------\n"+
                "Nombre: " + autor.getNombre() +"\n"+
                "Fecha de nacimiento: " + autor.getFechaNacimiento() +"\n"+
                "Fecha de fallecimiento: " + autor.getFechaFallecimiento() +"\n"+
                "Libros: " + autor.getLibros() +"\n"+
                "------------------------"));
    }

    private void listarAutoresVivosByAnio(){
        System.out.println("Escribe el año a buscar");
        var anio = teclado.nextInt();
        var autores = autorRepository.findAll();
        autores = autores.stream()
                .filter(autor -> autor.getFechaFallecimiento() > anio).toList();

        autores.forEach(autor ->   System.out.println("--------AUTOR--------\n"+
                "Nombre: " + autor.getNombre() +"\n"+
                "Fecha de nacimiento: " + autor.getFechaNacimiento() +"\n"+
                "Fecha de fallecimiento: " + autor.getFechaFallecimiento() +"\n"+
                "Libros: " + autor.getLibros() +"\n"+
                "------------------------"));
    }

    private void listarLibrosByIdioma(){
        System.out.println("""
                Ingresa el idioma para buscar los libros
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        var idioma = teclado.nextLine();
        var libros = libroRepository.findAll();
        libros = libros.stream().filter(libro -> libro.getLenguajes().equals(Idioma.fromString(idioma))).toList();

        libros.forEach(libro ->   System.out.println("--------LIBRO--------\n"+
                "Título: " + libro.getTitulo() +"\n"+
                "Autor: " + libro.getAutores().get(0) +"\n"+
                "Idioma: " + libro.getLenguajes() +"\n"+
                "Cantidad descargas: " + libro.getCantidadDescargas() +"\n"+
                "------------------------"));
    }

    private Libro getBookExist(String titulo) {
        return libroRepository.findByTitulo(titulo);
    }
    private List<Autor> getAutores(List<DatosAutor> autores){
        return autores.stream().map(autor -> new Autor(autor.name(), autor.birth_year(), autor.death_year())).toList();
    }

}
