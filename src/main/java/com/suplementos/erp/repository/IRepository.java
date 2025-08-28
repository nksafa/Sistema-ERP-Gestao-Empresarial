package com.suplementos.erp.repository;

import java.util.List;

public interface IRepository<T> {
    void salvar(int id, T entity);
    T buscarPorId(int id);
    void remover(int id);
    List<T> buscarTodos();
}