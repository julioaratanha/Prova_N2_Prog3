package br.edu.femass.dao;

import br.edu.femass.model.Artista;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class ArtistaDao extends DaoPostgres implements Dao<Artista> {

    @Override
    public Set<Artista> listar() throws Exception {
        String sql = "select * from artista order by nome";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        Set<Artista> artistas = new HashSet<>();
        while (rs.next()) {
            Artista artista = new Artista(rs.getLong("id"), rs.getString("nome"));
            artistas.add(artista);
        }

        return artistas;
    }

    @Override
    public void gravar(Artista value) throws Exception {
        String sql = "INSERT INTO artista (nome) VALUES (?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

    }

    @Override
    public void alterar(Artista value) throws Exception {
        String sql = "update artista set nome = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setLong(2, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void excluir(Artista value) throws Exception {
        String sql = "delete from artista where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
