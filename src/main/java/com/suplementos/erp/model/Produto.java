package com.suplementos.erp.model;

public record Produto(
        int id,
        String nome,
        String descricao,
        double preco,
        int quantidadeEmEstoque,
        int estoqueMinimo,
        Categoria categoria,
        Fornecedor fornecedor
) {}