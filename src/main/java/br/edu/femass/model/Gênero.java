package br.edu.femass.model;

import java.util.*;

import lombok.Data;

@Data
public class Gênero {

    public Gênero(){

    }

    public Gênero(Long id, String nome) {
        this.id=id;
        this.nome=nome;
    }

    private Long id;
    private String nome;

    @Override
    public String toString(){
        return this.nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gênero gênero = (Gênero) o;
        return this.nome.equals(gênero.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nome);
    }
}