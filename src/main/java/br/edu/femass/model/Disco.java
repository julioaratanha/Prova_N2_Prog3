package br.edu.femass.model;

import java.util.*;

import lombok.Data;

@Data
public class Disco {

    private Long id;
    private String titulo;
    private Double precoVenda;
    private Integer estoque;
    private Integer ano;
    private Gênero genero;
    private Artista artista;

    public Disco() {
        this.estoque=0;
    }

    @Override
    public String toString() {
        return this.artista.getNome() + " -> " + this.titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disco disco = (Disco) o;
        return Objects.equals(this.titulo, disco.titulo) && Objects.equals(this.artista.getNome(), disco.artista.getNome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, precoVenda, estoque, ano, genero, artista);
    }
}