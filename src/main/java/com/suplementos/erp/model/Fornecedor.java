package com.suplementos.erp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fornecedores")
public class Fornecedor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String contato;

    public Fornecedor() {}

    public Fornecedor(int id, String nome, String contato) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
    }

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

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}