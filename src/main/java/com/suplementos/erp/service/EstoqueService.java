package com.suplementos.erp.service;

import com.suplementos.erp.model.Produto;
import com.suplementos.erp.repository.ProdutoRepository;

public class EstoqueService {
    private final ProdutoRepository produtoRepository;

    public EstoqueService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void adicionarProduto(Produto produto) {
        produtoRepository.salvar(produto.id(), produto);
        System.out.println("Produto '" + produto.nome() + "' adicionado ao estoque.");
    }

    public void editarProduto(int id, Produto produtoAtualizado) {
        if (produtoRepository.buscarPorId(id) != null) {
            produtoRepository.salvar(id, produtoAtualizado);
            System.out.println("Produto editado com sucesso.");
        } else {
            System.out.println("Erro: Produto com ID " + id + " não encontrado.");
        }
    }

    public void removerProduto(int id) {
        produtoRepository.remover(id);
        System.out.println("Produto #" + id + " removido.");
    }

    public void atualizarEstoque(int id, int novaQuantidade) {
        produtoRepository.atualizarEstoque(id, novaQuantidade);
    }

    public void verificarAlertasDeEstoque() {
        System.out.println("\n--- ALERTA DE ESTOQUE ---");
        boolean alerta = false;
        for (Produto p : produtoRepository.buscarTodos()) {
            if (p.quantidadeEmEstoque() < p.estoqueMinimo()) {
                System.out.println("ALERTA: Produto '" + p.nome() + "' está abaixo do estoque mínimo (" + p.quantidadeEmEstoque() + "/" + p.estoqueMinimo() + ")!");
                alerta = true;
            }
        }
        if (!alerta) {
            System.out.println("Nenhum alerta de estoque no momento.");
        }
    }
    // Adicione este método na classe EstoqueService
    public void listarTodosOsProdutos() {
        System.out.println("\n--- LISTA DE PRODUTOS ---");
        for (Produto p : produtoRepository.buscarTodos()) {
            System.out.println("ID: " + p.id() + " | Nome: " + p.nome() + " | Estoque: " + p.quantidadeEmEstoque());
        }
        if (produtoRepository.buscarTodos().isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        }
    }

    public Produto buscarProdutoPorId(int id) {
        return produtoRepository.buscarPorId(id);
    }


    public ProdutoRepository getProdutoRepository() {
        return this.produtoRepository;
    }
}