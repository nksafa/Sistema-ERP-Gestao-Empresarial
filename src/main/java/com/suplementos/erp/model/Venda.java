package com.suplementos.erp.model;

import java.util.Date;
import java.util.List;

public record Venda(
        int id,
        Date dataVenda,
        Usuario cliente,
        Usuario funcionario,
        List<Produto> produtosVendidos,
        double valorTotal,
        FormaPagamento formaPagamento
) {}