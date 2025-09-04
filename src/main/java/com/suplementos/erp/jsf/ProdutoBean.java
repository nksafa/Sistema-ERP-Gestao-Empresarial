package com.suplementos.erp.jsf;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.ProdutoRepository;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@RequestScoped
public class ProdutoBean implements Serializable {

    private Produto produto = new Produto(0, "", "", 0.0, 0, 0, new Categoria(0, ""), new Fornecedor(0, "", ""));
    private ProdutoRepository produtoRepository = new ProdutoRepository();

    public void salvar() {
        // O Hibernate gera o ID automaticamente para nós
        produtoRepository.salvar(this.produto);

        // Adiciona a mensagem de sucesso
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Produto salvo com sucesso no banco de dados."));
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.buscarTodos();
    }

    // Getters e Setters
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}