package dev.luanfernandes.webclient.response;

import java.util.List;
import lombok.Builder;

@Builder
public record KeyCloakUserResponse(
        String lastName,
        boolean totp,
        KeyCloakAccess access,
        long createdTimestamp,
        boolean enabled,
        int notBefore,
        List<Object> disableableCredentialTypes,
        String firstName,
        boolean emailVerified,
        List<Object> requiredActions,
        KeyCloakAttributes attributes,
        String id,
        String email,
        String username) {}
