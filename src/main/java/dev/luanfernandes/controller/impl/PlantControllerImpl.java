package dev.luanfernandes.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.controller.PlantController;
import dev.luanfernandes.domain.request.PlantCreateRequest;
import dev.luanfernandes.domain.request.PlantUpdateRequest;
import dev.luanfernandes.domain.response.PlantResponse;
import dev.luanfernandes.service.PlantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlantControllerImpl implements PlantController {

    private final PlantService plantService;

    @Override
    public ResponseEntity<PlantResponse> create(PlantCreateRequest plant) {
        return status(CREATED).body(plantService.createPlant(plant));
    }

    @Override
    public ResponseEntity<List<PlantResponse>> getAll() {
        return ok(plantService.getAll());
    }

    @Override
    public ResponseEntity<PlantResponse> getById(Long id) {
        return ok(plantService.getById(id));
    }

    @Override
    public ResponseEntity<PlantResponse> updateById(Long id, PlantUpdateRequest plant) {
        plantService.updatePlant(id, plant);
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        plantService.deleteById(id);
        return noContent().build();
    }
}
