package com.suplementos.erp.repository;

import com.suplementos.erp.model.Produto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class ProdutoRepository implements IRepository<Produto> {

    private final SessionFactory sessionFactory;

    public ProdutoRepository() {
        // AQUI ESTÁ A MUDANÇA: Usamos a SessionFactory única
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Metodo para salvar um produto no banco de dados
    public void salvar(Produto produto) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(produto);
            session.getTransaction().commit();
        }
    }

    // Este metodo é obrigatório por causa da interface IRepository.
    // Ele simplesmente chama o metodo salvar correto, que usa o Hibernate.
    @Override
    public void salvar(int id, Produto produto) {
        // A lógica do Hibernate já lida com o ID
        salvar(produto);
    }

    @Override
    public Produto buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Produto.class, id);
        }
    }

    // Metodo para buscar o próximo ID (temporário, só para a lógica JSF)
    public int getNextId() {
        try (Session session = sessionFactory.openSession()) {
            List<Produto> produtos = session.createQuery("FROM Produto ORDER BY id DESC", Produto.class)
                    .setMaxResults(1)
                    .list();
            if (produtos.isEmpty()) {
                return 1;
            }
            return produtos.get(0).getId() + 1;
        }
    }

    @Override
    public void remover(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Produto produto = session.get(Produto.class, id);
            if (produto != null) {
                session.delete(produto);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Produto> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Produto", Produto.class).list();
        }
    }
}