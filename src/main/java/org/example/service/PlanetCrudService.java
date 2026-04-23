package org.example.service;

import org.example.entity.Planet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PlanetCrudService {

    private final SessionFactory sessionFactory;

    public PlanetCrudService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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

    public Optional<Planet> findById(String id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Planet.class, id));
        }
    }

    public List<Planet> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Planet ORDER BY id", Planet.class).list();
        }
    }

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