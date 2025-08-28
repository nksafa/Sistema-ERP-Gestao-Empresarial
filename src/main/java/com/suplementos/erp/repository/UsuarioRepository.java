package com.suplementos.erp.repository;

import com.suplementos.erp.model.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioRepository implements IRepository<Usuario> {
    private final Map<Integer, Usuario> dados = new HashMap<>();

    @Override
    public void salvar(int id, Usuario usuario) {
        dados.put(id, usuario);
    }

    @Override
    public Usuario buscarPorId(int id) {
        return dados.get(id);
    }

    @Override
    public void remover(int id) {
        dados.remove(id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(dados.values());
    }
}