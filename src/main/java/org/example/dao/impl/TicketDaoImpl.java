package org.example.dao.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.TicketDao;
import org.example.entity.Planet;
import org.example.entity.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TicketDaoImpl implements TicketDao {

    private final SessionFactory sessionFactory;

    public TicketDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Ticket save(Ticket ticket) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(ticket);
                session.flush();
                session.refresh(ticket);
                tx.commit();
                return ticket;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Ticket.class, id));
        }
    }

    @Override
    public List<Ticket> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT t FROM Ticket t " +
                            "JOIN FETCH t.client " +
                            "JOIN FETCH t.fromPlanet " +
                            "JOIN FETCH t.toPlanet " +
                            "ORDER BY t.id",
                    Ticket.class).list();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Ticket managed = session.find(Ticket.class, id);
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

    @Override
    public Ticket changeRoute(Long ticketId, Planet from, Planet to) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Ticket managed = session.find(Ticket.class, ticketId);
                if (managed == null) {
                    throw new EntityNotFoundException(
                            "Ticket not found: " + ticketId);
                }
                Planet managedFrom = session.getReference(Planet.class, from.getId());
                Planet managedTo = session.getReference(Planet.class, to.getId());
                managed.setFromPlanet(managedFrom);
                managed.setToPlanet(managedTo);
                tx.commit();
                return managed;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }
}