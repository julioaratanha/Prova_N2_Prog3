package br.edu.femass.model;

import java.util.*;


public class DetalheCompra {

    public DetalheCompra() {

    }

    private Compra compra;
    private Disco disco;
    private Integer quantidade;
    private Double precoUnitario;

    public void setQuantidade(Integer quantidade){
        if (quantidade<1) return;
        this.quantidade=quantidade;
    }

    public Integer getQuantidade(){
        return this.quantidade;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Disco getDisco() {
        return disco;
    }

    public void setDisco(Disco disco) {
        this.disco = disco;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    @Override
    public String toString() {
        return this.disco +
                " preço unitário=R$" + this.precoUnitario +
                " quantidade=" + this.quantidade;
    }
}