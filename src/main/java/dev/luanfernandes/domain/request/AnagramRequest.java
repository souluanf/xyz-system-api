package dev.luanfernandes.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AnagramRequest(
        @NotBlank(message = "A entrada não pode estar vazia")
                @Pattern(regexp = "^[a-zA-Z]+$", message = "A entrada deve conter apenas letras")
                String input) {}
