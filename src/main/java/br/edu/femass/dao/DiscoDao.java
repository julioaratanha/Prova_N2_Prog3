package br.edu.femass.dao;

import br.edu.femass.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class DiscoDao extends DaoPostgres implements Dao<Disco>{

    @Override
    public Set<Disco> listar() throws Exception {
        String sql = "select " +
                "disco.id as id, " +
                "disco.titulo as titulo, " +
                "disco.precovenda as precovenda, " +
                "disco.estoque as estoque, " +
                "disco.ano as ano, " +
                "genero.id as genero_id, " +
                "genero.nome as genero_nome, " +
                "artista.id as artista_id, " +
                "artista.nome as artista_nome " +
                "from disco inner join genero on disco.id_genero = genero.id " +
                "inner join artista on disco.id_artista = artista.id " +
                "order by artista.nome asc";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        Set<Disco> discos = new HashSet<>();

        while (rs.next()) {
            Disco disco = new Disco();
            disco.setId(rs.getLong("id"));
            disco.setTitulo(rs.getString("titulo"));
            disco.setPrecoVenda(rs.getDouble("precovenda"));
            disco.setEstoque(rs.getInt("estoque"));
            disco.setAno(rs.getInt("ano"));
            disco.setGenero(new Gênero(rs.getLong("genero_id"), rs.getString("genero_nome")));
            disco.setArtista(new Artista(rs.getLong("artista_id"), rs.getString("artista_nome")));
            discos.add(disco);
        }
        return discos;
    }

    @Override
    public void gravar(Disco value) throws Exception {
        String sql = "INSERT INTO disco (titulo, precovenda, estoque, ano, id_genero, id_artista) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getTitulo());
        ps.setDouble(2, value.getPrecoVenda());
        ps.setInt(3, value.getEstoque());
        ps.setInt(4, value.getAno());
        ps.setLong(5, value.getGenero().getId());
        ps.setLong(6, value.getArtista().getId());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void alterar(Disco value) throws Exception {
        String sql = "UPDATE disco SET titulo = ?, precovenda=?, estoque=?, ano=?, id_genero=?, id_artista=? WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getTitulo());
        ps.setDouble(2, value.getPrecoVenda());
        ps.setInt(3, value.getEstoque());
        ps.setInt(4, value.getAno());
        ps.setLong(5, value.getGenero().getId());
        ps.setLong(6, value.getArtista().getId());
        ps.setLong(7, value.getId());

        ps.executeUpdate();
    }

    @Override
    public void excluir(Disco value) throws Exception {
        String sql = "delete from disco where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
