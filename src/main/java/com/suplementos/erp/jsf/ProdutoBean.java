package com.suplementos.erp.jsf;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.ProdutoRepository;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class ProdutoBean implements Serializable {

    private Produto produto = new Produto();
    private ProdutoRepository produtoRepository = new ProdutoRepository();

    public void salvar() {
        produtoRepository.salvar(this.produto);
        // Reseta o formulário após salvar
        this.produto = new Produto();
        System.out.println("Produto salvo com sucesso!");
    }

    public String editar(Produto produto) {
        this.produto = produto;
        return "cadastro-produtos.xhtml?faces-redirect=true";
    }

    public void remover(Produto produto) {
        produtoRepository.remover(produto.getId());
        System.out.println("Produto removido com sucesso!");
    }

    public String novoProduto() {
        this.produto = new Produto(); // Cria um novo e vazio objeto Produto
        return "cadastro-produtos.xhtml?faces-redirect=true";
    }

    public List<Produto> getListaProdutos() {
        return produtoRepository.buscarTodos();
    }

    // Getters e Setters
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}