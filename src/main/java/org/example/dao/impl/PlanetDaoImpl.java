package org.example.dao.impl;

import org.example.dao.PlanetDao;
import org.example.entity.Planet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PlanetDaoImpl implements PlanetDao {

    private final SessionFactory sessionFactory;

    public PlanetDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Planet save(Planet planet) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(planet);
                tx.commit();
                return planet;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<Planet> findById(String id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Planet.class, id));
        }
    }

    @Override
    public List<Planet> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Planet ORDER BY id", Planet.class).list();
        }
    }

    @Override
    public Planet update(Planet planet) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Planet merged = session.merge(planet);
                tx.commit();
                return merged;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public boolean deleteById(String id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Planet managed = session.find(Planet.class, id);
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