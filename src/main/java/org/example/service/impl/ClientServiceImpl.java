package org.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.ClientDao;
import org.example.entity.Client;
import org.example.service.ClientService;

import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private static final int MAX_NAME_LENGTH = 200;

    private final ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public Client create(String name) {
        validateName(name);
        return clientDao.save(new Client(name));
    }

    @Override
    public Optional<Client> getById(Long id) {
        validateId(id);
        return clientDao.findById(id);
    }

    @Override
    public List<Client> getAll() {
        return clientDao.findAll();
    }

    @Override
    public Client rename(Long id, String newName) {
        validateId(id);
        validateName(newName);
        Client client = clientDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Client not found: " + id));
        client.setName(newName);
        return clientDao.update(client);
    }

    @Override
    public boolean delete(Long id) {
        validateId(id);
        return clientDao.deleteById(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "Name must be <= " + MAX_NAME_LENGTH + " characters");
        }
    }
}