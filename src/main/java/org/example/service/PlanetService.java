package org.example.service;

import org.example.entity.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetService {
    Planet create(String id, String name);
    Optional<Planet> getById(String id);
    List<Planet> getAll();
    Planet rename(String id, String newName);
    boolean delete(String id);
}