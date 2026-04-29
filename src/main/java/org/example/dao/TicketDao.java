package org.example.dao;

import org.example.entity.Planet;
import org.example.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketDao {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long id);
    List<Ticket> findAll();
    boolean deleteById(Long id);

    Ticket changeRoute(Long ticketId, Planet from, Planet to);
}