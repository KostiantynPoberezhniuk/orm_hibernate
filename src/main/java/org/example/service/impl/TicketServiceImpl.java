package org.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.ClientDao;
import org.example.dao.PlanetDao;
import org.example.dao.TicketDao;
import org.example.entity.Client;
import org.example.entity.Planet;
import org.example.entity.Ticket;
import org.example.service.TicketService;

import java.util.List;
import java.util.Optional;

public class TicketServiceImpl implements TicketService {

    private final TicketDao ticketDao;
    private final ClientDao clientDao;
    private final PlanetDao planetDao;

    public TicketServiceImpl(TicketDao ticketDao, ClientDao clientDao, PlanetDao planetDao) {
        this.ticketDao = ticketDao;
        this.clientDao = clientDao;
        this.planetDao = planetDao;
    }

    @Override
    public Ticket create(Long clientId, String fromPlanetId, String toPlanetId) {
        Client client = requireClient(clientId);
        Planet from = requirePlanet(fromPlanetId, "fromPlanetId");
        Planet to = requirePlanet(toPlanetId, "toPlanetId");
        requireDifferentPlanets(from, to);

        return ticketDao.save(new Ticket(client, from, to));
    }

    @Override
    public Optional<Ticket> getById(Long id) {
        validateTicketId(id);
        return ticketDao.findById(id);
    }

    @Override
    public List<Ticket> getAll() {
        return ticketDao.findAll();
    }

    @Override
    public Ticket changeRoute(Long ticketId, String fromPlanetId, String toPlanetId) {
        validateTicketId(ticketId);
        Planet from = requirePlanet(fromPlanetId, "fromPlanetId");
        Planet to = requirePlanet(toPlanetId, "toPlanetId");
        requireDifferentPlanets(from, to);

        return ticketDao.changeRoute(ticketId, from, to);
    }

    @Override
    public boolean delete(Long id) {
        validateTicketId(id);
        return ticketDao.deleteById(id);
    }

    private Client requireClient(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("clientId must not be null");
        }
        if (clientId <= 0) {
            throw new IllegalArgumentException("clientId must be positive");
        }
        return clientDao.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Client not found: " + clientId));
    }

    private Planet requirePlanet(String planetId, String fieldName) {
        if (planetId == null || planetId.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return planetDao.findById(planetId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Planet not found: " + planetId));
    }

    private void requireDifferentPlanets(Planet from, Planet to) {
        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException(
                    "fromPlanet and toPlanet must be different");
        }
    }

    private void validateTicketId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Ticket id must be positive");
        }
    }
}