package br.edu.femass.model;

import java.util.*;

import lombok.Data;

@Data
public class Pessoa {

    private Long id;
    private String nome;
    private String sobreNome;
    private String logradouroEnd;
    private Integer numeroEnd;
    private String complementoEnd;
    private String bairro;
    private String cidade;
    private String uf;
    private Integer ddd;
    private Long numeroTel;
    public Pessoa() {
    }
}