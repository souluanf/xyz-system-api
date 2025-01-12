package dev.luanfernandes.webclient.response;


public record KeyCloakAccess(boolean manageGroupMembership, boolean view, boolean mapRoles, boolean impersonate, boolean manage) {}
