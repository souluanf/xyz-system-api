package dev.luanfernandes.domain.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.luanfernandes.domain.entity.Plant;
import dev.luanfernandes.domain.request.PlantCreateRequest;
import dev.luanfernandes.domain.request.PlantUpdateRequest;
import dev.luanfernandes.domain.response.PlantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = SPRING)
public interface PlantMapper {

    Plant map(PlantCreateRequest plantCreateRequest);

    PlantResponse map(Plant produto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void map(PlantUpdateRequest pedidoItem, @MappingTarget Plant plant);
}
