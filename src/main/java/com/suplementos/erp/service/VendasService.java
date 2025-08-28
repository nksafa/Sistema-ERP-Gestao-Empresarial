package com.suplementos.erp.service;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.repository.VendaRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendasService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    private static int nextVendaId = 1;

    public VendasService(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
    }

    public Venda realizarVenda(Usuario cliente, Usuario funcionario, List<Integer> idsProdutos, FormaPagamento formaPagamento) {
        List<Produto> produtosVendidos = new ArrayList<>();
        double valorTotal = 0.0;

        for (Integer id : idsProdutos) {
            Produto p = produtoRepository.buscarPorId(id);
            if (p != null && p.quantidadeEmEstoque() > 0) {
                produtosVendidos.add(p);
                valorTotal += p.preco();
                // Aqui é onde o estoque é alterado no banco de dados
                produtoRepository.atualizarEstoque(p.id(), p.quantidadeEmEstoque() - 1);
            } else {
                System.out.println("Erro: Produto com ID " + id + " não disponível.");
                return null;
            }
        }

        Venda novaVenda = new Venda(nextVendaId++, new Date(), cliente, funcionario, produtosVendidos, valorTotal, formaPagamento);
        vendaRepository.salvar(novaVenda.id(), novaVenda);

        System.out.println("\n--- Recibo da Venda #" + novaVenda.id() + " ---");
        System.out.println("Data: " + novaVenda.dataVenda());
        System.out.println("Funcionário: " + novaVenda.funcionario().nome());
        System.out.println("Itens:");
        for (Produto p : produtosVendidos) {
            System.out.println("- " + p.nome() + " | R$" + String.format("%.2f", p.preco()));
        }
        System.out.println("Total: R$" + String.format("%.2f", novaVenda.valorTotal()));
        System.out.println("------------------------------------");

        return novaVenda;
    }
    // Adicione este método na classe VendasService
    public void listarHistoricoDeVendas() {
        System.out.println("\n--- HISTÓRICO DE VENDAS ---");
        List<Venda> vendas = vendaRepository.buscarTodos();
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda realizada.");
        } else {
            for (Venda v : vendas) {
                System.out.println("Venda #" + v.id() + " | Data: " + v.dataVenda() + " | Total: R$" + String.format("%.2f", v.valorTotal()));
            }
        }
    }

}