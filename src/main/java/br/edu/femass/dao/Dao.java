package br.edu.femass.dao;

import java.util.Set;

public interface Dao<T> {
    public Set<T> listar() throws Exception;
    public void gravar(T value) throws Exception;
    public void alterar(T value) throws Exception;
    public void excluir(T value) throws Exception;
}
