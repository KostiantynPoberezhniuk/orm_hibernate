package org.example.dao.impl;

import org.example.dao.ClientDao;
import org.example.entity.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    private final SessionFactory sessionFactory;

    public ClientDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Client save(Client client) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(client);
                tx.commit();
                return client;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<Client> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Client.class, id));
        }
    }

    @Override
    public List<Client> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Client ORDER BY id", Client.class).list();
        }
    }

    @Override
    public Client update(Client client) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Client merged = session.merge(client);
                tx.commit();
                return merged;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Client managed = session.find(Client.class, id);
                if (managed == null) {
                    tx.rollback();
                    return false;
                }
                session.remove(managed);
                tx.commit();
                return true;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }
}