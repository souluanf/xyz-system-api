package dev.luanfernandes.domain.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.luanfernandes.domain.entity.Salesperson;
import dev.luanfernandes.domain.response.SalespersonResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface SalesPersonMapper {

    SalespersonResponse map(Salesperson salesperson);
}
