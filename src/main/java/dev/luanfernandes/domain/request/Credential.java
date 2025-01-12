package dev.luanfernandes.domain.request;

import lombok.Builder;

@Builder
public record Credential(String type, String value, Boolean temporary) {}
