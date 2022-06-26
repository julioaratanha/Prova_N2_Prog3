package br.edu.femass.dao;

import br.edu.femass.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static br.edu.femass.LojaDiscos.discoDao;
import static br.edu.femass.LojaDiscos.pessoaDao;

public class CompraDao extends DaoPostgres{

    public Set<Compra> listar() throws Exception {
        String sql = "select * from compra order by data";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        Set<Compra> compras = new HashSet<>();
        while (rs.next()) {
            Compra compra = new Compra();
            compra.setNf(rs.getLong("notafiscal"));
            compra.setData(rs.getDate("data"));
            Set<Pessoa> pessoas = pessoaDao.listar();
            for (Pessoa pessoa: pessoas){
                if (pessoa.getId()==rs.getLong("id_pessoa")){
                    compra.setFornecedor((Fornecedor) pessoa);
                    break;
                }
            }
            sql = "select * from detalhecompra where notafiscal_compra="+compra.getNf();
            PreparedStatement ps1 = getPreparedStatement(sql, false);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()){
                DetalheCompra detalheCompra = new DetalheCompra();
                Set<Disco> discos = discoDao.listar();
                for (Disco disco: discos){
                    if (disco.getId()==rs1.getLong("id_disco")){
                        detalheCompra.setDisco(disco);
                        break;
                    }
                }
                detalheCompra.setCompra(compra);
                detalheCompra.setQuantidade(rs1.getInt("quantidade"));
                detalheCompra.setPrecoUnitario(rs1.getDouble("precounitario"));
                compra.getItens().add(detalheCompra);
            }
            compras.add(compra);
        }
        return compras;
    }


    public void gravar(Compra value) throws Exception {
        String sql = "INSERT INTO compra (data, id_pessoa) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setDate(1, value.getData());
        ps.setLong(2, value.getFornecedor().getId());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setNf(rs.getLong(1));
        for (DetalheCompra detalheCompra : value.getItens()){
            sql = "INSERT INTO detalhecompra (quantidade, precounitario, id_disco, notafiscal_compra) VALUES (?,?,?,?)";
            PreparedStatement ps1 = getPreparedStatement(sql, true);
            ps1.setInt(1, detalheCompra.getQuantidade());
            ps1.setDouble(2, detalheCompra.getPrecoUnitario());
            ps1.setLong(3, detalheCompra.getDisco().getId());
            ps1.setLong(4, value.getNf());
            ps1.executeUpdate();
        }
    }

}
