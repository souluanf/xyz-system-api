package dev.luanfernandes.domain.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PlantUpdateRequest(
        @NotNull(message = "O código é obrigatório")
                @NotNull(message = "O código é obrigatório")
                @Digits(integer = 10, fraction = 0, message = "O código deve ser numérico")
                @Positive(message = "O preço deve ser positivo.")
                Long code,
        @NotBlank(message = "A descrição é obrigatória")
                @Size(max = 10, message = "A descrição pode ter no máximo 10 caracteres")
                String description) {}
