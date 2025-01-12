package dev.luanfernandes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.adapter.PlantRepositoryAdapter;
import dev.luanfernandes.domain.entity.Plant;
import dev.luanfernandes.domain.exception.EntityAlreadyExistsException;
import dev.luanfernandes.domain.exception.NotFoundException;
import dev.luanfernandes.domain.mapper.PlantMapper;
import dev.luanfernandes.domain.request.PlantCreateRequest;
import dev.luanfernandes.domain.request.PlantUpdateRequest;
import dev.luanfernandes.domain.response.PlantResponse;
import dev.luanfernandes.repository.PlantRepository;
import dev.luanfernandes.service.impl.PlantServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {
    @Mock
    PlantRepositoryAdapter plantRepository;

    @Mock
    PlantMapper plantMapper;

    @InjectMocks
    PlantServiceImpl plantService;

    @Test
    void createPlant_ShouldCreatePlant_WhenCodeDoesNotExist() {
        PlantCreateRequest request = new PlantCreateRequest(1L, "Plant Name");
        Plant plant = Plant.builder().code(1L).description("Plant Name").build();
        PlantResponse expectedResponse = new PlantResponse(1L, "Plant Name");

        when(plantRepository.existsByCode(request.code())).thenReturn(false);
        when(plantMapper.map(request)).thenReturn(plant);
        when(plantRepository.save(plant)).thenReturn(plant);
        when(plantMapper.map(plant)).thenReturn(expectedResponse);

        PlantResponse response = plantService.createPlant(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(plantRepository).save(plant);
    }

    @Test
    void createPlant_ShouldThrowException_WhenCodeAlreadyExists() {
        PlantCreateRequest request = new PlantCreateRequest(1L, "Plant Name");

        when(plantRepository.existsByCode(request.code())).thenReturn(true);

        assertThatThrownBy(() -> plantService.createPlant(request))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("Plant with code 1 already exists");

        verify(plantRepository, never()).save(any(Plant.class));
    }

    @Test
    void getAll_ShouldReturnAllPlants() {
        List<Plant> plants = List.of(
                Plant.builder().code(1L).description("Plant A").build(),
                Plant.builder().code(2L).description("Plant B").build());
        List<PlantResponse> expectedResponses =
                List.of(new PlantResponse(1L, "Plant A"), new PlantResponse(2L, "Plant B"));

        when(plantRepository.findAll()).thenReturn(plants);
        when(plantMapper.map(plants.get(0))).thenReturn(expectedResponses.get(0));
        when(plantMapper.map(plants.get(1))).thenReturn(expectedResponses.get(1));

        List<PlantResponse> responses = plantService.getAll();

        assertThat(responses).hasSize(2).containsExactlyInAnyOrderElementsOf(expectedResponses);
    }

    @Test
    void getById_ShouldReturnPlant_WhenPlantExists() {
        Plant plant = Plant.builder().code(1L).description("Plant Name").build();
        PlantResponse expectedResponse = new PlantResponse(1L, "Plant Name");

        when(plantRepository.findById(1L)).thenReturn(Optional.of(plant));
        when(plantMapper.map(plant)).thenReturn(expectedResponse);

        PlantResponse response = plantService.getById(1L);

        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void getById_ShouldThrowException_WhenPlantDoesNotExist() {
        when(plantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plantService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Plant with id 1 not found");
    }

    @Test
    void updatePlant_ShouldUpdatePlant_WhenPlantExists() {
        PlantUpdateRequest request = new PlantUpdateRequest(1L, "Plant Name");
        Plant plant = Plant.builder().code(1L).description("Plant Name").build();
        when(plantRepository.findById(1L)).thenReturn(Optional.of(plant));
        plantService.updatePlant(1L, request);

        verify(plantRepository).save(plant);
        verify(plantMapper).map(request, plant);
    }

    @Test
    void deleteById_ShouldDeletePlant_WhenPlantExists() {
        Plant plant = Plant.builder().code(1L).description("Plant Name").build();
        when(plantRepository.findById(1L)).thenReturn(Optional.of(plant));

        plantService.deleteById(1L);
        verify(plantRepository).delete(plant);
    }

    @Test
    void deleteById_ShouldThrowException_WhenPlantDoesNotExist() {
        when(plantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plantService.deleteById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Plant with id 1 not found");
    }
}
