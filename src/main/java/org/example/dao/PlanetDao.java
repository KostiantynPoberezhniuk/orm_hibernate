package org.example.dao;

import org.example.entity.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetDao {
    Planet save(Planet planet);
    Optional<Planet> findById(String id);
    List<Planet> findAll();
    Planet update(Planet planet);
    boolean deleteById(String id);
}