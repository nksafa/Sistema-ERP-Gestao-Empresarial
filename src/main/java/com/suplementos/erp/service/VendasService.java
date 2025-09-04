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
    private final EstoqueService estoqueService; // Adicionamos a dependência para o EstoqueService

    private static int nextVendaId = 1;

    public VendasService(VendaRepository vendaRepository, ProdutoRepository produtoRepository, EstoqueService estoqueService) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueService = estoqueService;
    }

    public Venda realizarVenda(Usuario cliente, Usuario funcionario, List<Integer> idsProdutos, FormaPagamento formaPagamento) {
        List<Produto> produtosVendidos = new ArrayList<>();
        double valorTotal = 0.0;

        for (Integer id : idsProdutos) {
            Produto p = produtoRepository.buscarPorId(id);
            if (p != null && p.getQuantidadeEmEstoque() > 0) {
                produtosVendidos.add(p);
                valorTotal += p.getPreco();

                // Agora chamamos o método do EstoqueService
                estoqueService.atualizarEstoque(p.getId(), -1);
            } else {
                System.out.println("Erro: Produto com ID " + id + " não disponível.");
                return null;
            }
        }

        Venda novaVenda = new Venda(nextVendaId++, new Date(), cliente, funcionario, produtosVendidos, valorTotal, formaPagamento);
        // Salvando a nova venda
        vendaRepository.salvar(novaVenda.getId(), novaVenda);

        System.out.println("\n--- Recibo da Venda #" + novaVenda.getId() + " ---");
        System.out.println("Data: " + novaVenda.getDataVenda());
        System.out.println("Funcionário: " + novaVenda.getFuncionario().getNome());
        System.out.println("Itens:");
        for (Produto p : produtosVendidos) {
            System.out.println("- " + p.getNome() + " | R$" + String.format("%.2f", p.getPreco()));
        }
        System.out.println("Total: R$" + String.format("%.2f", novaVenda.getValorTotal()));
        System.out.println("------------------------------------");

        return novaVenda;
    }

    public void listarHistoricoDeVendas() {
        System.out.println("\n--- HISTÓRICO DE VENDAS ---");
        List<Venda> vendas = vendaRepository.buscarTodos();
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda realizada.");
        } else {
            for (Venda v : vendas) {
                System.out.println("Venda #" + v.getId() + " | Data: " + v.getDataVenda() + " | Total: R$" + String.format("%.2f", v.getValorTotal()));
            }
        }
    }
}