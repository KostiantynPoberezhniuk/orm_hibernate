package org.example.service;

import org.example.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client create(String name);
    Optional<Client> getById(Long id);
    List<Client> getAll();
    Client rename(Long id, String newName);
    boolean delete(Long id);
}