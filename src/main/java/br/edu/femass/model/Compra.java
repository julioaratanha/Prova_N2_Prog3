package br.edu.femass.model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Compra {

    public Compra() {
    }

    private Long nf;
    private Date data;
    private Fornecedor fornecedor;
    private Set<DetalheCompra> itens=new HashSet<>();

    public Double totalCompra(){
        Double total=0.0;
        for (DetalheCompra item : this.itens){
            total=total+(item.getPrecoUnitario()*item.getQuantidade());
        }
        return total;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatadorDeData = new SimpleDateFormat("dd/MM/yyyy");
        return formatadorDeData.format(this.data)+" - Nota Fiscal="+ this.nf +
                " / Fornecedor=" + this.fornecedor;
    }
}