package dev.luanfernandes.service;

import dev.luanfernandes.domain.request.UserCreateRequest;
import dev.luanfernandes.domain.request.UserUpdateRequest;
import dev.luanfernandes.domain.response.UserResponse;
import java.util.List;

public interface UserService {
    void create(UserCreateRequest request, String authorization);

    List<UserResponse> findAll(String authorization);

    void delete(String username, String authorization);

    void update(String username, UserUpdateRequest user, String authorization);

    UserResponse findByUsername(String username, String authorization);
}
