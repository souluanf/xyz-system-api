package dev.luanfernandes.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record UserUpdateRequest(
        @NotBlank @Email String email,
        @NotBlank String name,
        @NotBlank String password,
        String address,
        String phone) {}
