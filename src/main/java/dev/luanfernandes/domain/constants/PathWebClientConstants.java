package dev.luanfernandes.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathWebClientConstants {
    public static final String BASE_ADMIN_REALMS = "/admin/realms/{realm}";
    public static final String USERS = BASE_ADMIN_REALMS + "/users";
    public static final String USER_BY_ID = USERS + "/{userId}";
    public static final String USER_BY_FILTER = USERS + "?search={searchTerm}";
    public static final String USER_ROLE_MAPPINGS = USER_BY_ID + "/role-mappings/realm";
    public static final String ROLES = BASE_ADMIN_REALMS + "/roles";
    public static final String ROLE_BY_NAME = ROLES + "/{roleName}";

    public static final String BASE_REALMS = "/realms/{realm}";
    public static final String TOKEN = BASE_REALMS + "/protocol/openid-connect/token";
}
