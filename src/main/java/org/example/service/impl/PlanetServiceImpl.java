package org.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.PlanetDao;
import org.example.entity.Planet;
import org.example.service.PlanetService;

import java.util.List;
import java.util.Optional;

public class PlanetServiceImpl implements PlanetService {

    private static final int MAX_NAME_LENGTH = 500;
    private static final int MAX_ID_LENGTH = 50;

    private final PlanetDao planetDao;

    public PlanetServiceImpl(PlanetDao planetDao) {
        this.planetDao = planetDao;
    }

    @Override
    public Planet create(String id, String name) {
        validateId(id);
        validateName(name);
        return planetDao.save(new Planet(id, name));
    }

    @Override
    public Optional<Planet> getById(String id) {
        validateId(id);
        return planetDao.findById(id);
    }

    @Override
    public List<Planet> getAll() {
        return planetDao.findAll();
    }

    @Override
    public Planet rename(String id, String newName) {
        validateId(id);
        validateName(newName);
        Planet planet = planetDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Planet not found: " + id));
        planet.setName(newName);
        return planetDao.update(planet);
    }

    @Override
    public boolean delete(String id) {
        validateId(id);
        return planetDao.deleteById(id);
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be blank");
        }
        if (id.length() > MAX_ID_LENGTH) {
            throw new IllegalArgumentException(
                    "Id must be <= " + MAX_ID_LENGTH + " characters");
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