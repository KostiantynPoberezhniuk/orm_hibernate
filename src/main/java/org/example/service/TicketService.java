package org.example.service;

import org.example.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket create(Long clientId, String fromPlanetId, String toPlanetId);
    Optional<Ticket> getById(Long id);
    List<Ticket> getAll();
    Ticket changeRoute(Long ticketId, String fromPlanetId, String toPlanetId);
    boolean delete(Long id);
}