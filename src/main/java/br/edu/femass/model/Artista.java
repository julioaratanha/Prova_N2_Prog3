package br.edu.femass.model;

import java.util.*;

import lombok.Data;

@Data
public class Artista {

    private Long id;
    private String nome;

    public Artista(){
    }

    public Artista(Long id, String nome) {
        this.id=id;
        this.nome=nome;
    }

    public String toString() {
        return this.nome.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artista artista = (Artista) o;
        return this.nome.equals(artista.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

}