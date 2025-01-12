package dev.luanfernandes.repository;

import dev.luanfernandes.domain.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    boolean existsByCode(Long code);
}
