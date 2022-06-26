package br.edu.femass.model;

import java.util.*;


public class DetalheVenda {


    public DetalheVenda() {
    }

    private Venda venda;
    private Disco disco;
    private Integer quantidade;
    private Double precoUnitario;

    public Venda getVenda() {
        return this.venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Disco getDisco() {
        return disco;
    }

    public void setDisco(Disco disco) {
        this.disco = disco;
    }

    public Integer getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        if (quantidade<1) return;
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return this.precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    @Override
    public String toString() {
        return this.disco +
                " (Preço de Venda: R$"+this.disco.getPrecoVenda()+")" +
                " quantidade=" + this.quantidade;
    }
}