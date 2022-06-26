package br.edu.femass.dao;

import br.edu.femass.model.Artista;
import br.edu.femass.model.Gênero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;


public class GeneroDao extends DaoPostgres implements Dao<Gênero>{
    @Override
    public Set<Gênero> listar() throws Exception {
        String sql = "select * from genero order by nome";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        Set<Gênero> generos = new HashSet<>();
        while (rs.next()) {
            Gênero genero = new Gênero(rs.getLong("id"),rs.getString("nome"));
            generos.add(genero);
        }

        return generos;
    }

    @Override
    public void gravar(Gênero value) throws Exception {
        String sql = "INSERT INTO genero (nome) VALUES (?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void alterar(Gênero value) throws Exception {
        String sql = "update genero set nome = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setLong(2, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void excluir(Gênero value) throws Exception {
        String sql = "delete from genero where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
