package dev.luanfernandes.service.impl;

import static java.lang.String.format;

import dev.luanfernandes.adapter.PlantRepositoryAdapter;
import dev.luanfernandes.domain.entity.Plant;
import dev.luanfernandes.domain.exception.EntityAlreadyExistsException;
import dev.luanfernandes.domain.exception.NotFoundException;
import dev.luanfernandes.domain.mapper.PlantMapper;
import dev.luanfernandes.domain.request.PlantCreateRequest;
import dev.luanfernandes.domain.request.PlantUpdateRequest;
import dev.luanfernandes.domain.response.PlantResponse;
import dev.luanfernandes.service.PlantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {

    private final PlantRepositoryAdapter plantRepositoryAdapter;
    private final PlantMapper plantMapper;

    @Transactional
    @Override
    public PlantResponse createPlant(PlantCreateRequest plantCreateRequest) {
        if (plantRepositoryAdapter.existsByCode(plantCreateRequest.code())) {
            throw new EntityAlreadyExistsException(
                    format("Plant with code %s already exists", plantCreateRequest.code()));
        }
        Plant plant = savePlant(plantMapper.map(plantCreateRequest));
        return plantMapper.map(plant);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PlantResponse> getAll() {
        return plantRepositoryAdapter.findAll().stream().map(plantMapper::map).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PlantResponse getById(Long plantId) {
        Plant plant = findPlantByIdOrThrow(plantId);
        return plantMapper.map(plant);
    }

    @Transactional
    @Override
    public void updatePlant(Long plantId, PlantUpdateRequest plantUpdateRequest) {
        Plant existingPlant = findPlantByIdOrThrow(plantId);
        plantMapper.map(plantUpdateRequest, existingPlant);
        savePlant(existingPlant);
    }

    @Transactional
    @Override
    public void deleteById(Long plantId) {
        plantRepositoryAdapter.findById(plantId).ifPresentOrElse(plantRepositoryAdapter::delete, () -> {
            throw new NotFoundException(format("Plant with id %d not found", plantId));
        });
    }

    private Plant findPlantByIdOrThrow(Long plantId) {
        return plantRepositoryAdapter
                .findById(plantId)
                .orElseThrow(() -> new NotFoundException(format("Plant with id %d not found", plantId)));
    }

    private Plant savePlant(Plant plant) {
        return plantRepositoryAdapter.save(plant);
    }
}
