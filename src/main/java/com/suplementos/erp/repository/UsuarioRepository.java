package com.suplementos.erp.repository;

import com.suplementos.erp.model.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class UsuarioRepository implements IRepository<Usuario> {

    private final SessionFactory sessionFactory;

    public UsuarioRepository() {
        // AQUI ESTÁ A MUDANÇA: Usamos a SessionFactory única
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Metodo para buscar um usuário pelo nome, usando Hibernate
    public Usuario buscarPorNome(String nome) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Usuario WHERE nome = :nome", Usuario.class)
                    .setParameter("nome", nome)
                    .uniqueResult();
        }
    }

    // Métodos de Persistência com Hibernate (que você já tinha)
    @Override
    public void salvar(int id, Usuario usuario) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(usuario);
            session.getTransaction().commit();
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Usuario.class, id);
        }
    }

    @Override
    public void remover(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, id);
            if (usuario != null) {
                session.delete(usuario);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Usuario> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Usuario", Usuario.class).list();
        }
    }
}