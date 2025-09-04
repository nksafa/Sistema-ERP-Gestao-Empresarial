package com.suplementos.erp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "produtos")
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidadeEmEstoque;
    private int estoqueMinimo;
    private String categoria;
    private String fornecedor;

    // O Hibernate exige um construtor vazio
    public Produto() {}

    // Construtor completo para a criação de novos objetos

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Produto(int id, String nome, String descricao, double preco, int quantidadeEmEstoque, int estoqueMinimo, Categoria categoria, Fornecedor fornecedor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria.getNome(); // Salva apenas o nome da categoria
        this.fornecedor = fornecedor.getNome(); // Salva apenas o nome do fornecedor
    }

    // Construtor para casos de edição ou criação sem ID
    public Produto(String nome, String descricao, double preco, int quantidadeEmEstoque, int estoqueMinimo, Categoria categoria, Fornecedor fornecedor) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria.getNome();
        this.fornecedor = fornecedor.getNome();
    }

}