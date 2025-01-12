package dev.luanfernandes.webclient;

import static dev.luanfernandes.domain.constants.PathWebClientConstants.ROLE_BY_NAME;
import static dev.luanfernandes.domain.constants.PathWebClientConstants.TOKEN;
import static dev.luanfernandes.domain.constants.PathWebClientConstants.USERS;
import static dev.luanfernandes.domain.constants.PathWebClientConstants.USER_BY_FILTER;
import static dev.luanfernandes.domain.constants.PathWebClientConstants.USER_BY_ID;
import static dev.luanfernandes.domain.constants.PathWebClientConstants.USER_ROLE_MAPPINGS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.luanfernandes.webclient.request.KeyCloakRoleRequest;
import dev.luanfernandes.webclient.request.KeyCloakTokenRequest;
import dev.luanfernandes.webclient.request.KeyCloakUserRequest;
import dev.luanfernandes.webclient.request.KeyCloakUserUpdateRequest;
import dev.luanfernandes.webclient.response.KeyCloakRoleResponse;
import dev.luanfernandes.webclient.response.KeyCloakTokenResponse;
import dev.luanfernandes.webclient.response.KeyCloakUserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KeycloakClient {

    @Value("${spring.security.oauth2.resource-server.jwt.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resource-server.jwt.realm}")
    private String realm;

    private final RestClient keycloak;

    public KeyCloakTokenResponse getToken(KeyCloakTokenRequest tokenRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", tokenRequest.username());
        formData.add("password", tokenRequest.password());
        formData.add("client_id", clientId);
        formData.add("grant_type", "password");
        return keycloak.post()
                .uri(TOKEN, realm)
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .body(formData)
                .retrieve()
                .body(KeyCloakTokenResponse.class);
    }

    public void createUser(KeyCloakUserRequest userRequest, String accessToken) {
        keycloak.post()
                .uri(USERS, realm)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, accessToken)
                .body(userRequest)
                .retrieve()
                .toBodilessEntity();
    }

    public KeyCloakRoleResponse getRole(String roleName, String accessToken) {
        return keycloak.get()
                .uri(ROLE_BY_NAME, realm, roleName)
                .header(AUTHORIZATION, accessToken)
                .retrieve()
                .body(KeyCloakRoleResponse.class);
    }

    public void assignRoleToUser(String userId, KeyCloakRoleRequest roleRequest, String accessToken) {
        keycloak.post()
                .uri(USER_ROLE_MAPPINGS, realm, userId)
                .header(AUTHORIZATION, accessToken)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(List.of(roleRequest))
                .retrieve()
                .toBodilessEntity();
    }

    public List<KeyCloakUserResponse> getUsers(String accessToken) {
        return keycloak.get()
                .uri(USERS, realm)
                .header(AUTHORIZATION, accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<KeyCloakUserResponse> searchUsers(String searchTerm, String accessToken) {
        return keycloak.get()
                .uri(USER_BY_FILTER, realm, searchTerm)
                .header(AUTHORIZATION, accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<KeyCloakRoleResponse> getUserRealmRoles(String userId, String accessToken) {
        return keycloak.get()
                .uri(USER_ROLE_MAPPINGS, realm, userId)
                .header(AUTHORIZATION, accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void updateUser(String userId, KeyCloakUserUpdateRequest userUpdateRequest, String accessToken) {
        keycloak.put()
                .uri(USER_BY_ID, realm, userId)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, accessToken)
                .body(userUpdateRequest)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteUser(String userId, String accessToken) {
        keycloak.delete()
                .uri(USER_BY_ID, realm, userId)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, accessToken)
                .retrieve()
                .toBodilessEntity();
    }
}
