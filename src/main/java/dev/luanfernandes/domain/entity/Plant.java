package dev.luanfernandes.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "plants")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plant extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long code;

    @Column(length = 10)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(code, plant.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
