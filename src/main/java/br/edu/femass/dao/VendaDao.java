package br.edu.femass.dao;

import br.edu.femass.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static br.edu.femass.LojaDiscos.discoDao;
import static br.edu.femass.LojaDiscos.pessoaDao;

public class VendaDao extends DaoPostgres{

    public Set<Venda> listar() throws Exception {
        String sql = "select * from venda order by data";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        Set<Venda> vendas = new HashSet<>();
        while (rs.next()) {
            Venda venda = new Venda();
            venda.setNf(rs.getLong("notafiscal"));
            venda.setData(rs.getDate("data"));
            Set<Pessoa> pessoas = pessoaDao.listar();
            for (Pessoa pessoa: pessoas){
                if (pessoa.getId()==rs.getLong("id_pessoa")){
                    venda.setCliente((Cliente) pessoa);
                    break;
                }
            }
            sql = "select * from detalhevenda where notafiscal_venda="+venda.getNf();
            PreparedStatement ps1 = getPreparedStatement(sql, false);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()){
                DetalheVenda detalheVenda = new DetalheVenda();
                Set<Disco> discos = discoDao.listar();
                for (Disco disco: discos){
                    if (disco.getId()==rs1.getLong("id_disco")){
                        detalheVenda.setDisco(disco);
                        break;
                    }
                }
                detalheVenda.setVenda(venda);
                detalheVenda.setQuantidade(rs1.getInt("quantidade"));
                detalheVenda.setPrecoUnitario(rs1.getDouble("precounitario"));
                venda.getItens().add(detalheVenda);
            }
            vendas.add(venda);
        }
        return vendas;
    }


    public void gravar(Venda value) throws Exception {
        String sql = "INSERT INTO venda (data, id_pessoa) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setDate(1, value.getData());
        ps.setLong(2, value.getCliente().getId());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setNf(rs.getLong(1));
        for (DetalheVenda detalheVenda : value.getItens()){
            sql = "INSERT INTO detalhevenda (quantidade, precounitario, id_disco, notafiscal_venda) VALUES (?,?,?,?)";
            PreparedStatement ps1 = getPreparedStatement(sql, true);
            ps1.setInt(1, detalheVenda.getQuantidade());
            ps1.setDouble(2, detalheVenda.getPrecoUnitario());
            ps1.setLong(3, detalheVenda.getDisco().getId());
            ps1.setLong(4, value.getNf());
            ps1.executeUpdate();
        }
    }

}
