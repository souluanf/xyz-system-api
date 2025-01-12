package dev.luanfernandes.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "salespeople")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salesperson extends BaseEntity {

    private String name;
    private int age;
    private double salary;
}
