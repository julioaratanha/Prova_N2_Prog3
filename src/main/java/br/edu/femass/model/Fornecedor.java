package br.edu.femass.model;

import lombok.Data;

@Data
public class Fornecedor extends Pessoa {

    private String cnpj;
    public Fornecedor() {
    }

    @Override
    public String toString(){
        return this.getNome()+" "+this.getSobreNome()+" CNPJ="+this.getCnpj();
    }
}