package com.suplementos.erp.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.suplementos.erp.model.Produto;
import com.suplementos.erp.model.Categoria;
import com.suplementos.erp.model.Fornecedor;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository implements IRepository<Produto> {

    private final MongoCollection<Document> produtosCollection;

    // Precisamos de um contador para o ID, já que o MongoDB não cria um "_id" com o mesmo formato que usamos
    private static int nextId = 1;

    public ProdutoRepository() {
        MongoDatabase database = MongoConnection.getDatabase();
        this.produtosCollection = database.getCollection("produtos");
        // Encontra o próximo ID disponível
        Document lastProduct = produtosCollection.find().sort(new Document("_id", -1)).limit(1).first();
        if (lastProduct != null) {
            nextId = lastProduct.getInteger("_id") + 1;
        }
    }

    @Override
    public void salvar(int id, Produto produto) {
        Document doc = new Document("_id", id)
                .append("nome", produto.nome())
                .append("descricao", produto.descricao())
                .append("preco", produto.preco())
                .append("quantidadeEmEstoque", produto.quantidadeEmEstoque())
                .append("estoqueMinimo", produto.estoqueMinimo())
                .append("categoria", produto.categoria().nome())
                .append("fornecedor", produto.fornecedor().nome());

        // Usa upsert: se o documento com o ID existir, ele atualiza; se não, ele insere
        produtosCollection.replaceOne(Filters.eq("_id", id), doc, new com.mongodb.client.model.ReplaceOptions().upsert(true));
    }

    // Método para atualizar o estoque de um produto específico
    public void atualizarEstoque(int id, int novaQuantidade) {
        produtosCollection.updateOne(
                Filters.eq("_id", id),
                Updates.set("quantidadeEmEstoque", novaQuantidade)
        );
    }

    // Método para pegar o próximo ID disponível
    public int getNextId() {
        return nextId++;
    }

    @Override
    public Produto buscarPorId(int id) {
        Document doc = produtosCollection.find(Filters.eq("_id", id)).first();
        if (doc != null) {
            return converterDocumentoParaProduto(doc);
        }
        return null;
    }

    @Override
    public void remover(int id) {
        produtosCollection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public List<Produto> buscarTodos() {
        List<Produto> produtos = new ArrayList<>();
        for (Document doc : produtosCollection.find()) {
            produtos.add(converterDocumentoParaProduto(doc));
        }
        return produtos;
    }

    // Método auxiliar para converter um Document do MongoDB para um objeto Produto
    private Produto converterDocumentoParaProduto(Document doc) {
        Categoria categoria = new Categoria(0, doc.getString("categoria"));
        Fornecedor fornecedor = new Fornecedor(0, doc.getString("fornecedor"), "");

        return new Produto(
                doc.getInteger("_id"),
                doc.getString("nome"),
                doc.getString("descricao"),
                doc.getDouble("preco"),
                doc.getInteger("quantidadeEmEstoque"),
                doc.getInteger("estoqueMinimo"),
                categoria,
                fornecedor
        );
    }
}