package dev.luanfernandes.adapter;

import dev.luanfernandes.domain.entity.Plant;
import dev.luanfernandes.repository.PlantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlantRepositoryAdapterImpl implements PlantRepositoryAdapter {

    private final PlantRepository plantRepository;

    public PlantRepositoryAdapterImpl(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public boolean existsByCode(Long code) {
        return plantRepository.existsByCode(code);
    }

    @Override
    public List<Plant> findAll() {
        return plantRepository.findAll();
    }

    @Override
    public Optional<Plant> findById(Long plantId) {
        return plantRepository.findById(plantId);
    }

    @Override
    public Plant save(Plant plant) {
        return plantRepository.save(plant);
    }

    @Override
    public void delete(Plant plant) {
        plantRepository.delete(plant);
    }
}