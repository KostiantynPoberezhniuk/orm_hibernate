package org.example.dao;

import org.example.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {
    Client save(Client client);
    Optional<Client> findById(Long id);
    List<Client> findAll();
    Client update(Client client);
    boolean deleteById(Long id);
}