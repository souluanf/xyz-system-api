package dev.luanfernandes.adapter;


import dev.luanfernandes.domain.entity.Plant;

import java.util.List;
import java.util.Optional;

public interface PlantRepositoryAdapter {
    boolean existsByCode(Long code);
    List<Plant> findAll();
    Optional<Plant> findById(Long plantId);
    Plant save(Plant plant);
    void delete(Plant plant);
}