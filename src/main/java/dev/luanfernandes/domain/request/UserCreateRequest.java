package dev.luanfernandes.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserCreateRequest(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank String name,
        @NotBlank String password,
        @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres") String address,
        @Pattern(
                        regexp = "^[1-9]{2}\\d{8,9}$",
                        message =
                                "Informe um telefone válido no formato: DDD + número com 8 ou 9 dígitos (ex.: 1120202121 ou 11920202121).")
                String phone,
        boolean isAdmin) {}
