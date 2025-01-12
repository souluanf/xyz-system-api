package dev.luanfernandes.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.controller.UserController;
import dev.luanfernandes.domain.request.UserCreateRequest;
import dev.luanfernandes.domain.request.UserUpdateRequest;
import dev.luanfernandes.domain.response.UserResponse;
import dev.luanfernandes.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<Void> create(UserCreateRequest request, String authorization) {
        userService.create(request, authorization);
        return status(CREATED).build();
    }

    @Override
    public ResponseEntity<List<UserResponse>> findAll(String authorization) {
        return ok(userService.findAll(authorization));
    }

    @Override
    public ResponseEntity<UserResponse> findByUsername(String username, String authorization) {
        return ok(userService.findByUsername(username, authorization));
    }

    @Override
    public ResponseEntity<Void> update(String username, UserUpdateRequest user, String authorization) {
        userService.update(username, user, authorization);
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> delete(String username, String authorization) {
        userService.delete(username, authorization);
        return noContent().build();
    }
}
