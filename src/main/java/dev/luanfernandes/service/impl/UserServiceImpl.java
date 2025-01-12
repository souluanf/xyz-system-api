package dev.luanfernandes.service.impl;

import static dev.luanfernandes.domain.enums.UserRole.ADMIN;
import static dev.luanfernandes.domain.enums.UserRole.USER;

import dev.luanfernandes.domain.enums.UserRole;
import dev.luanfernandes.domain.exception.AdminUserDeletionException;
import dev.luanfernandes.domain.exception.NotFoundException;
import dev.luanfernandes.domain.mapper.KeycloakMapper;
import dev.luanfernandes.domain.request.Credential;
import dev.luanfernandes.domain.request.UserCreateRequest;
import dev.luanfernandes.domain.request.UserUpdateRequest;
import dev.luanfernandes.domain.response.UserResponse;
import dev.luanfernandes.service.UserService;
import dev.luanfernandes.webclient.KeycloakClient;
import dev.luanfernandes.webclient.request.KeyCloakRoleRequest;
import dev.luanfernandes.webclient.request.KeyCloakUserRequest;
import dev.luanfernandes.webclient.request.KeyCloakUserUpdateRequest;
import dev.luanfernandes.webclient.response.KeyCloakRoleResponse;
import dev.luanfernandes.webclient.response.KeyCloakUserResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakClient webClient;
    private final KeycloakMapper keycloakMapper;

    @Override
    public void create(UserCreateRequest request, String authorization) {
        KeyCloakUserRequest userRequest = KeyCloakUserRequest.builder()
                .username(request.username())
                .email(request.email())
                .firstName(request.name())
                .emailVerified(true)
                .enabled(true)
                .credentials(List.of(Credential.builder()
                        .type("password")
                        .value(request.password())
                        .build()))
                .attributes(buildAttributes(request))
                .build();

        webClient.createUser(userRequest, authorization);

        String userId = getUserIdByUsername(request.username(), authorization);
        assignRoleToUser(userId, request.isAdmin() ? ADMIN : USER, authorization);
    }

    @Override
    public List<UserResponse> findAll(String authorization) {
        return webClient.getUsers(authorization).stream().map(keycloakMapper::map).toList();
    }

    @Override
    public void delete(String username, String authorization) {
        if ("admin".equals(username)) {
            throw new AdminUserDeletionException("Cannot delete main admin user");
        }
        String userId = getUserIdByUsername(username, authorization);
        webClient.deleteUser(userId, authorization);
    }

    @Override
    public void update(String username, UserUpdateRequest request, String authorization) {
        String userId = getUserIdByUsername(username, authorization);
        KeyCloakUserUpdateRequest userRequest = KeyCloakUserUpdateRequest.builder()
                .email(request.email())
                .firstName(request.name())
                .credentials(List.of(Credential.builder()
                        .type("password")
                        .value(request.password())
                        .build()))
                .emailVerified(true)
                .attributes(buildAttributes(request))
                .build();
        webClient.updateUser(userId, userRequest, authorization);
    }

    @Override
    public UserResponse findByUsername(String username, String authorization) {
        List<KeyCloakUserResponse> userResponses = webClient.searchUsers(username, authorization);
        if (userResponses.isEmpty()) {
            throw new NotFoundException("User with username " + username + " not found");
        }
        KeyCloakUserResponse keyCloakUserResponse = userResponses.getFirst();
        return keycloakMapper.map(keyCloakUserResponse);
    }

    private String getUserIdByUsername(String username, String authorization) {
        return findByUsername(username, authorization).id();
    }

    private void assignRoleToUser(String userId, UserRole role, String authorization) {
        KeyCloakRoleResponse roleResponse = webClient.getRole(role.name(), authorization);
        webClient.assignRoleToUser(
                userId,
                KeyCloakRoleRequest.builder()
                        .id(roleResponse.id())
                        .name(roleResponse.name())
                        .build(),
                authorization);
    }

    private Map<String, String> buildAttributes(Object request) {
        Map<String, String> attributes = new HashMap<>();
        if (request instanceof UserCreateRequest createRequest) {
            if (createRequest.address() != null && !createRequest.address().isBlank()) {
                attributes.put("address", createRequest.address());
            }
            if (createRequest.phone() != null && !createRequest.phone().isBlank()) {
                attributes.put("phone", createRequest.phone());
            }
        } else if (request instanceof UserUpdateRequest updateRequest) {
            if (updateRequest.address() != null && !updateRequest.address().isBlank()) {
                attributes.put("address", updateRequest.address());
            }
            if (updateRequest.phone() != null && !updateRequest.phone().isBlank()) {
                attributes.put("phone", updateRequest.phone());
            }
        }
        return attributes;
    }
}
