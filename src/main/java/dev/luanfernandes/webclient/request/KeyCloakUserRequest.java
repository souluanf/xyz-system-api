package dev.luanfernandes.webclient.request;

import dev.luanfernandes.domain.request.Credential;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record KeyCloakUserRequest(
        String username,
        Boolean enabled,
        String firstName,
        String email,
        Boolean emailVerified,
        List<Credential> credentials,
        Map<String, String> attributes
) {}
