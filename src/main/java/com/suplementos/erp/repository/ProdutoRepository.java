package com.suplementos.erp.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.suplementos.erp.model.Categoria;
import com.suplementos.erp.model.Fornecedor;
import com.suplementos.erp.model.Produto;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository implements IRepository<Produto> {

    private final MongoCollection<Document> produtosCollection;
    private static int nextId = 1;

    public ProdutoRepository() {
        MongoDatabase database = MongoConnection.getDatabase();
        this.produtosCollection = database.getCollection("produtos");

        // Encontra o próximo ID disponível.
        // Tentamos encontrar o maior ID numérico para continuar a sequência.
        // Se a coleção estiver vazia ou com IDs de outros tipos, o nextId permanece 1.
        try {
            Document lastProduct = produtosCollection.find().sort(new Document("_id", -1)).limit(1).first();
            if (lastProduct != null) {
                // Se o ID for numérico, continuamos a sequência
                if (lastProduct.get("_id") instanceof Integer) {
                    nextId = lastProduct.getInteger("_id") + 1;
                }
            }
        } catch (Exception e) {
            // Em caso de erro (como um ObjectId no _id), reiniciamos o contador.
            nextId = 1;
        }

    }
// Dentro da classe ProdutoRepository.java

    // Este método é público agora para ser usado em outras classes
    public int getNextId() {
        return nextId++;
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

        produtosCollection.replaceOne(Filters.eq("_id", id), doc, new ReplaceOptions().upsert(true));
    }

    public void atualizarEstoque(int id, int novaQuantidade) {
        produtosCollection.updateOne(
                Filters.eq("_id", id),
                Updates.set("quantidadeEmEstoque", novaQuantidade)
        );
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

    private Produto converterDocumentoParaProduto(Document doc) {
        Categoria categoria = new Categoria(0, doc.getString("categoria"));
        Fornecedor fornecedor = new Fornecedor(0, doc.getString("fornecedor"), "");

        // Trata o _id para garantir que é um Integer
        Object idObject = doc.get("_id");
        int id;
        if (idObject instanceof Integer) {
            id = (Integer) idObject;
        } else {
            // Em caso de _id ser um ObjectId, vamos usar 0 ou gerar um novo ID.
            // Para o nosso caso, usaremos 0, pois a lógica de ID deve ser gerida
            // pela nossa aplicação.
            id = 0;
        }

        return new Produto(
                id,
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