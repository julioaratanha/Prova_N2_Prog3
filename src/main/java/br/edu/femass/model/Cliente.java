package br.edu.femass.model;

import lombok.Data;

@Data
public class Cliente extends Pessoa {

    public String cpf;

    public Cliente() {
    }

    @Override
    public String toString(){
        return this.getNome()+" "+this.getSobreNome()+" CPF="+this.getCpf();
    }
}