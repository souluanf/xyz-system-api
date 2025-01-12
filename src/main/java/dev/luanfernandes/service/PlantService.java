package dev.luanfernandes.service;

import dev.luanfernandes.domain.request.PlantCreateRequest;
import dev.luanfernandes.domain.request.PlantUpdateRequest;
import dev.luanfernandes.domain.response.PlantResponse;
import java.util.List;

public interface PlantService {
    PlantResponse createPlant(PlantCreateRequest plantCreateRequest);

    List<PlantResponse> getAll();

    PlantResponse getById(Long plantId);

    void updatePlant(Long plantId, PlantUpdateRequest plantUpdateRequest);

    void deleteById(Long plantId);
}
