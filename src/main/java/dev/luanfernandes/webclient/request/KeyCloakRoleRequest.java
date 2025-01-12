package dev.luanfernandes.webclient.request;

import lombok.Builder;

@Builder
public record KeyCloakRoleRequest(String id, String name) {}
