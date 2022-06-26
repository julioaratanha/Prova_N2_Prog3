package br.edu.femass.dao;

import java.sql.*;

public abstract class DaoPostgres {
    protected static String ENDERECO = "localhost";
    protected static String BD = "loja_de_discos";
    protected static String PORTA = "5432";
    protected static String USUARIO = "postgres";
    protected static String SENHA = "postgres";
    protected static Connection con;

    public static void getConexao() {
        String url = "jdbc:postgresql://" + ENDERECO + ":" + PORTA + "/" + BD;
        try {
            con = DriverManager.getConnection(url, USUARIO, SENHA);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected PreparedStatement getPreparedStatement(String sql, Boolean insercao) throws Exception {
        if (insercao) {
            return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            return con.prepareStatement(sql);
        }
    }

}
