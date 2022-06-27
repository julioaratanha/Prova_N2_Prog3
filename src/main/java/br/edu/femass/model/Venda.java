package br.edu.femass.model;


import java.text.SimpleDateFormat;
import java.util.Set;
import java.sql.Date;
import java.util.HashSet;

import lombok.Data;

@Data
public class Venda {


    public Venda() {
    }

    private Long nf;
    private Date data;
    private Cliente cliente;
    private Set<DetalheVenda> itens=new HashSet<>();

    public Double totalVenda(){
        Double total=0.0;
        for (DetalheVenda item : this.itens){
            total=total+(item.getPrecoUnitario()*item.getQuantidade());
        }
        return total;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatadorDeData = new SimpleDateFormat("dd/MM/yyyy");
        return formatadorDeData.format(this.data)+" - Nota Fiscal="+ this.nf +
                " / Cliente=" + this.cliente;
    }
}