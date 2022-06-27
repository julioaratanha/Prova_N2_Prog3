package br.edu.femass.dao;

import br.edu.femass.model.Cliente;
import br.edu.femass.model.Fornecedor;
import br.edu.femass.model.Pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PessoaDao extends DaoPostgres implements Dao<Pessoa>{

    @Override
    public Set<Pessoa> listar() throws Exception {
        String sql = "select " +
                "pessoa.id as id, " +
                "pessoa.nome as nome, " +
                "pessoa.sobrenome as sobrenome, " +
                "pessoa.logradouro as logradouro, " +
                "pessoa.numend as numend, " +
                "pessoa.complemento as complemento, " +
                "pessoa.bairro as bairro, " +
                "pessoa.cidade as cidade, " +
                "pessoa.uf as uf, " +
                "pessoa.ddd as ddd, " +
                "pessoa.numtel as numtel, " +
                "cliente.cpf as cpf " +
                "from pessoa inner join cliente on pessoa.id = cliente.id_pessoa " +
                "order by pessoa.nome asc";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        Set<Pessoa> pessoas = new HashSet<>();

        while (rs.next()) {
            Cliente cliente = new Cliente();
            cliente.setId(rs.getLong("id"));
            cliente.setCpf(rs.getString("cpf"));
            cliente.setNome(rs.getString("nome"));
            cliente.setSobreNome(rs.getString("sobrenome"));
            cliente.setLogradouroEnd(rs.getString("logradouro"));
            cliente.setNumeroEnd(rs.getInt("numend"));
            cliente.setComplementoEnd(rs.getString("complemento"));
            cliente.setBairro(rs.getString("bairro"));
            cliente.setCidade(rs.getString("cidade"));
            cliente.setUf(rs.getString("uf"));
            cliente.setDdd(rs.getInt("ddd"));
            cliente.setNumeroTel(rs.getLong("numtel"));
            pessoas.add(cliente);
        }

        sql = "select " +
                "pessoa.id as id, " +
                "pessoa.nome as nome, " +
                "pessoa.sobrenome as sobrenome, " +
                "pessoa.logradouro as logradouro, " +
                "pessoa.numend as numend, " +
                "pessoa.complemento as complemento, " +
                "pessoa.bairro as bairro, " +
                "pessoa.cidade as cidade, " +
                "pessoa.uf as uf, " +
                "pessoa.ddd as ddd, " +
                "pessoa.numtel as numtel, " +
                "fornecedor.cnpj as cnpj " +
                "from pessoa inner join fornecedor on pessoa.id = fornecedor.id_pessoa " +
                "order by pessoa.nome asc";

        ps = getPreparedStatement(sql, false);
        rs = ps.executeQuery();

        while (rs.next()) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(rs.getLong("id"));
            fornecedor.setCnpj(rs.getString("cnpj"));
            fornecedor.setNome(rs.getString("nome"));
            fornecedor.setSobreNome(rs.getString("sobrenome"));
            fornecedor.setLogradouroEnd(rs.getString("logradouro"));
            fornecedor.setNumeroEnd(rs.getInt("numend"));
            fornecedor.setComplementoEnd(rs.getString("complemento"));
            fornecedor.setBairro(rs.getString("bairro"));
            fornecedor.setCidade(rs.getString("cidade"));
            fornecedor.setUf(rs.getString("uf"));
            fornecedor.setDdd(rs.getInt("ddd"));
            fornecedor.setNumeroTel(rs.getLong("numtel"));
            pessoas.add(fornecedor);
        }
        return pessoas;
    }

    @Override
    public void gravar(Pessoa value) throws Exception {
        String sql = "INSERT INTO pessoa (nome, sobrenome, logradouro, numend, complemento, bairro, cidade, uf, ddd, numtel) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getSobreNome());
        ps.setString(3, value.getLogradouroEnd());
        ps.setInt(4, value.getNumeroEnd());
        ps.setString(5, value.getComplementoEnd());
        ps.setString(6, value.getBairro());
        ps.setString(7, value.getCidade());
        ps.setString(8, value.getUf());
        ps.setInt(9, value.getDdd());
        ps.setLong(10, value.getNumeroTel());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

        if (value instanceof Cliente){
            sql = "INSERT INTO cliente (id_pessoa, cpf) VALUES (?,?)";
            ps = getPreparedStatement(sql, true);
            ps.setString(2, ((Cliente) value).getCpf());
        }else{
            sql = "INSERT INTO fornecedor (id_pessoa, cnpj) VALUES (?,?)";
            ps = getPreparedStatement(sql, true);
            ps.setString(2, ((Fornecedor) value).getCnpj());
        }
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void alterar(Pessoa value) throws Exception {
        String sql = "UPDATE pessoa SET nome = ?, sobrenome=?, logradouro=?, numend=?, complemento=?, bairro=?, cidade=?, uf=?, ddd=?, numtel=? WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getSobreNome());
        ps.setString(3, value.getLogradouroEnd());
        ps.setInt(4, value.getNumeroEnd());
        ps.setString(5, value.getComplementoEnd());
        ps.setString(6, value.getBairro());
        ps.setString(7, value.getCidade());
        ps.setString(8, value.getUf());
        ps.setInt(9, value.getDdd());
        ps.setLong(10, value.getNumeroTel());
        ps.setLong(11, value.getId());
        ps.executeUpdate();

        if (value instanceof Cliente){
            sql = "UPDATE cliente SET cpf = ? where id_pessoa = ?";
            ps = getPreparedStatement(sql, true);
            ps.setString(1, ((Cliente) value).getCpf());
        }else{
            sql = "UPDATE fornecedor SET cnpj = ? where id_pessoa = ?";
            ps = getPreparedStatement(sql, true);
            ps.setString(1, ((Fornecedor) value).getCnpj());
        }
        ps.setLong(2, value.getId());
        ps.executeUpdate();
    }


    @Override
    public void excluir(Pessoa value) throws Exception {

        try {
            String sql = "delete from pessoa where id = ?";
            PreparedStatement ps2 = con.prepareStatement(sql);

            ps2.setLong(1, value.getId());
            ps2.executeUpdate();

            sql = "";
            if (value instanceof Cliente) {
                sql = "delete from cliente where id_pessoa = ?";
            } else {
                sql = "delete from fornecedor where id_pessoa = ?";
            }
            PreparedStatement ps1 = con.prepareStatement(sql);
            ps1.setLong(1, value.getId());
            ps1.executeUpdate();
        } catch (SQLException exception) {
            throw exception;
        }
    }
}
