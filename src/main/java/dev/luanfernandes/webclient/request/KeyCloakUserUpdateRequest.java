package dev.luanfernandes.webclient.request;

import dev.luanfernandes.domain.request.Credential;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record KeyCloakUserUpdateRequest(
        String firstName,
        String lastName,
        boolean emailVerified,
        String email,
        List<Credential> credentials,
        Map<String, String> attributes) {}
