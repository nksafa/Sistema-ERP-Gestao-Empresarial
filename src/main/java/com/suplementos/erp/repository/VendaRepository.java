package com.suplementos.erp.repository;

import com.suplementos.erp.model.Venda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendaRepository implements IRepository<Venda> {
    private final Map<Integer, Venda> dados = new HashMap<>();

    @Override
    public void salvar(int id, Venda venda) {
        dados.put(id, venda);
    }

    @Override
    public Venda buscarPorId(int id) {
        return dados.get(id);
    }

    @Override
    public void remover(int id) {
        dados.remove(id);
    }

    @Override
    public List<Venda> buscarTodos() {
        return new ArrayList<>(dados.values());
    }
}