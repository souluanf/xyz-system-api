package dev.luanfernandes.webclient.response;

import lombok.Builder;

@Builder
public record KeyCloakRoleResponse(
        boolean clientRole, boolean composite, String name, String description, String id, String containerId) {}
