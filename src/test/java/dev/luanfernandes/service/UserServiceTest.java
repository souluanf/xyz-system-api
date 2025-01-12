package dev.luanfernandes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import dev.luanfernandes.domain.enums.UserRole;
import dev.luanfernandes.domain.exception.AdminUserDeletionException;
import dev.luanfernandes.domain.exception.NotFoundException;
import dev.luanfernandes.domain.mapper.KeycloakMapper;
import dev.luanfernandes.domain.request.Credential;
import dev.luanfernandes.domain.request.UserCreateRequest;
import dev.luanfernandes.domain.request.UserUpdateRequest;
import dev.luanfernandes.domain.response.UserResponse;
import dev.luanfernandes.service.impl.UserServiceImpl;
import dev.luanfernandes.webclient.KeycloakClient;
import dev.luanfernandes.webclient.request.KeyCloakRoleRequest;
import dev.luanfernandes.webclient.request.KeyCloakUserRequest;
import dev.luanfernandes.webclient.response.KeyCloakAccess;
import dev.luanfernandes.webclient.response.KeyCloakAttributes;
import dev.luanfernandes.webclient.response.KeyCloakRoleResponse;
import dev.luanfernandes.webclient.response.KeyCloakUserResponse;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private KeycloakClient webClient;

    @Mock
    private KeycloakMapper mapper;

    private final String authorization = "Bearer token";
    private UserUpdateRequest updateRequest;
    private KeyCloakUserResponse keyCloakUserResponse;

    @BeforeEach
    void setUp() {
        updateRequest = new UserUpdateRequest(
                "user1@example.com", "User One Updated", "newpassword", "456 Avenue", "0987654321");
        keyCloakUserResponse = KeyCloakUserResponse.builder()
                .username("user1")
                .totp(false)
                .access(new KeyCloakAccess(true, true, true, true, true))
                .email("user1@example.com")
                .attributes(new KeyCloakAttributes(List.of("123 Street"), List.of("1234567890")))
                .firstName("User One")
                .lastName("Doe")
                .emailVerified(true)
                .enabled(true)
                .id("id")
                .requiredActions(List.of())
                .createdTimestamp(1234567890L)
                .disableableCredentialTypes(List.of())
                .notBefore(0)
                .build();
    }

    @Test
    @DisplayName("Should create a user and assign the correct role")
    void shouldCreateUserAndAssignRole() {
        UserCreateRequest request = new UserCreateRequest(
                "testuser",
                "testuser@example.com",
                "Test User",
                "password123",
                "123 Test Street",
                "1234567890",
                true);

        KeyCloakUserRequest userRequest = KeyCloakUserRequest.builder()
                .username("testuser")
                .email("testuser@example.com")
                .firstName("Test User")
                .emailVerified(true)
                .enabled(true)
                .credentials(List.of(Credential.builder()
                        .type("password")
                        .value("password123")
                        .build()))
                .attributes(Map.of("address", "123 Test Street", "phone", "1234567890"))
                .build();

        KeyCloakRoleResponse roleResponse = KeyCloakRoleResponse.builder()
                .id("role-id")
                .name("ADMIN")
                .build();

        KeyCloakUserResponse testuser = KeyCloakUserResponse.builder()
                .id("user-id")
                .username("testuser")
                .build();

        UserResponse userResponse = new UserResponse(
                "testuser",
                "testuser@example.com",
                "Test User",
                "123 Test Street",
                "1234567890",
                "user-id");

        doNothing().when(webClient).createUser(userRequest, authorization);
        when(webClient.searchUsers("testuser", authorization)).thenReturn(List.of(testuser));
        when(mapper.map(testuser)).thenReturn(userResponse);
        when(webClient.getRole("ADMIN", authorization)).thenReturn(roleResponse);
        doNothing().when(webClient).assignRoleToUser("user-id",
                KeyCloakRoleRequest.builder().id("role-id").name("ADMIN").build(),
                authorization);

        userService.create(request, authorization);

        verify(webClient, times(1)).createUser(userRequest, authorization);
        verify(webClient, times(1)).searchUsers("testuser", authorization);
        verify(mapper, times(1)).map(testuser);
        verify(webClient, times(1)).getRole("ADMIN", authorization);
        verify(webClient, times(1)).assignRoleToUser(
                "user-id",
                KeyCloakRoleRequest.builder().id("role-id").name("ADMIN").build(),
                authorization
        );
    }

    @Test
    @DisplayName("Should create a user and assign USER role")
    void shouldCreateUserAndAssignUserRole() {
        UserCreateRequest request = new UserCreateRequest(
                "testuser",
                "testuser@example.com",
                "Test User",
                "password123",
                "123 Test Street",
                "1234567890",
                false);

        KeyCloakUserRequest userRequest = KeyCloakUserRequest.builder()
                .username("testuser")
                .email("testuser@example.com")
                .firstName("Test User")
                .emailVerified(true)
                .enabled(true)
                .credentials(List.of(Credential.builder()
                        .type("password")
                        .value("password123")
                        .build()))
                .attributes(Map.of("address", "123 Test Street", "phone", "1234567890"))
                .build();

        KeyCloakRoleResponse roleResponse = KeyCloakRoleResponse.builder()
                .id("role-id")
                .name("USER")
                .build();

        KeyCloakUserResponse testuser = KeyCloakUserResponse.builder()
                .id("user-id")
                .username("testuser")
                .build();

        UserResponse userResponse = new UserResponse(
                "testuser",
                "testuser@example.com",
                "Test User",
                "123 Test Street",
                "1234567890",
                "user-id");

        doNothing().when(webClient).createUser(userRequest, authorization);
        when(webClient.searchUsers("testuser", authorization)).thenReturn(List.of(testuser));
        when(mapper.map(testuser)).thenReturn(userResponse);
        when(webClient.getRole("USER", authorization)).thenReturn(roleResponse);
        doNothing().when(webClient).assignRoleToUser("user-id",
                KeyCloakRoleRequest.builder().id("role-id").name("USER").build(),
                authorization);

        userService.create(request, authorization);

        verify(webClient, times(1)).createUser(userRequest, authorization);
        verify(webClient, times(1)).searchUsers("testuser", authorization);
        verify(mapper, times(1)).map(testuser);
        verify(webClient, times(1)).getRole("USER", authorization);
        verify(webClient, times(1)).assignRoleToUser(
                "user-id",
                KeyCloakRoleRequest.builder().id("role-id").name("USER").build(),
                authorization
        );
    }

    @Test
    @DisplayName("Should throw exception when deleting admin user")
    void shouldThrowExceptionWhenDeletingAdminUser() {
        assertThatThrownBy(() -> userService.delete("admin", authorization))
                .isInstanceOf(AdminUserDeletionException.class)
                .hasMessage("Cannot delete main admin user");
    }

    @Test
    @DisplayName("Should delete user by username")
    void shouldDeleteUser() {
        when(webClient.searchUsers("user1", authorization)).thenReturn(List.of(keyCloakUserResponse));
        when(mapper.map(keyCloakUserResponse))
                .thenReturn(
                        new UserResponse("user1", "user1@example.com", "User One", "123 Street", "1234567890", "id"));
        userService.delete("user1", authorization);
        verify(webClient, times(1)).deleteUser("id", authorization);
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindByUsername() {
        when(webClient.searchUsers("user1", authorization)).thenReturn(List.of(keyCloakUserResponse));
        when(mapper.map(keyCloakUserResponse))
                .thenReturn(
                        new UserResponse("user1", "user1@example.com", "User One", "123 Street", "1234567890", "id"));

        UserResponse userResponse = userService.findByUsername("user1", authorization);

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.username()).isEqualTo("user1");
    }

    @Test
    @DisplayName("Should throw NotFoundException when user not found")
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        when(webClient.searchUsers("unknown", authorization)).thenReturn(List.of());
        assertThatThrownBy(() -> userService.findByUsername("unknown", authorization))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with username unknown not found");
    }

    @Test
    @DisplayName("Should assign role to user using reflection")
    void shouldAssignRoleToUserUsingReflection() throws Exception {
        String userId = "user-id";
        UserRole role = UserRole.ADMIN;
        KeyCloakRoleResponse roleResponse = KeyCloakRoleResponse.builder()
                .id("role-id")
                .name("ADMIN")
                .build();
        when(webClient.getRole(role.name(), authorization)).thenReturn(roleResponse);
        doNothing().when(webClient).assignRoleToUser(anyString(), any(KeyCloakRoleRequest.class), anyString());

        Method method = UserServiceImpl.class.getDeclaredMethod("assignRoleToUser", String.class, UserRole.class, String.class);
        method.setAccessible(true);

        method.invoke(userService, userId, role, authorization);

        verify(webClient, times(1)).getRole(role.name(), authorization);
        verify(webClient, times(1)).assignRoleToUser(
                userId,
                KeyCloakRoleRequest.builder().id("role-id").name("ADMIN").build(),
                authorization
        );
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        when(webClient.searchUsers("user1", authorization)).thenReturn(List.of(keyCloakUserResponse));
        when(mapper.map(keyCloakUserResponse))
                .thenReturn(
                        new UserResponse("user1", "user1@example.com", "User One", "123 Street", "1234567890", "id"));

        userService.update("user1", updateRequest, authorization);

        verify(webClient, times(1)).updateUser(eq("id"), any(), eq(authorization));
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        List<KeyCloakUserResponse> keyCloakUserResponses = List.of(keyCloakUserResponse);
        when(webClient.getUsers(authorization)).thenReturn(keyCloakUserResponses);
        when(mapper.map(keyCloakUserResponse))
                .thenReturn(
                        new UserResponse("user1", "user1@example.com", "User One", "123 Street", "1234567890", "id"));
        List<UserResponse> users = userService.findAll(authorization);
        assertThat(users).hasSize(1);
    }

    @Test
    @DisplayName("Should build attributes for UserCreateRequest using reflection")
    void shouldBuildAttributesForUserCreateRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user1",
                "user1@example.com",
                "User One",
                "password",
                "123 Test Street",
                "1234567890",
                true);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes)
                .isNotNull()
                .hasSize(2)
                .containsEntry("address", "123 Test Street")
                .containsEntry("phone", "1234567890");
    }

    @Test
    @DisplayName("Should return empty attributes for null values in requests")
    void shouldReturnEmptyAttributesForNullValues() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user3",
                "user3@example.com",
                "User Three",
                "password",
                null,
                null,
                false);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("Should build attributes for UserUpdateRequest using reflection")
    void shouldBuildAttributesForUserUpdateRequest() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "user2@example.com",
                "User Two Updated",
                "newpassword",
                "456 Updated Avenue",
                "0987654321");

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, userUpdateRequest);

        assertThat(attributes)
                .isNotNull()
                .hasSize(2)
                .containsEntry("address", "456 Updated Avenue")
                .containsEntry("phone", "0987654321");
    }

    @Test
    @DisplayName("Should build attributes with full data for UserCreateRequest")
    void shouldBuildAttributesWithFullDataForUserCreateRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user1",
                "user1@example.com",
                "User One",
                "password",
                "123 Test Street",
                "1234567890",
                true);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes)
                .isNotNull()
                .containsEntry("address", "123 Test Street")
                .containsEntry("phone", "1234567890");
    }

    @Test
    @DisplayName("Should build attributes with partial data for UserCreateRequest")
    void shouldBuildAttributesWithPartialDataForUserCreateRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user1",
                "user1@example.com",
                "User One",
                "password",
                "123 Test Street", // Only address provided
                null,
                true);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes)
                .isNotNull()
                .containsEntry("address", "123 Test Street")
                .doesNotContainKey("phone");
    }

    @Test
    @DisplayName("Should return empty attributes for empty UserCreateRequest")
    void shouldReturnEmptyAttributesForEmptyUserCreateRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user1",
                "user1@example.com",
                "User One",
                "password",
                null,
                null,
                true);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should build attributes with full data for UserUpdateRequest")
    void shouldBuildAttributesWithFullDataForUserUpdateRequest() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "user2@example.com",
                "User Two Updated",
                "newpassword",
                "456 Updated Avenue",
                "0987654321");

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, userUpdateRequest);

        assertThat(attributes)
                .isNotNull()
                .containsEntry("address", "456 Updated Avenue")
                .containsEntry("phone", "0987654321");
    }

    @Test
    @DisplayName("Should build attributes with partial data for UserUpdateRequest")
    void shouldBuildAttributesWithPartialDataForUserUpdateRequest() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "user2@example.com",
                "User Two Updated",
                "newpassword",
                "456 Updated Avenue", // Only address provided
                null);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, userUpdateRequest);

        assertThat(attributes)
                .isNotNull()
                .containsEntry("address", "456 Updated Avenue")
                .doesNotContainKey("phone");
    }

    @Test
    @DisplayName("Should return empty attributes for empty UserUpdateRequest")
    void shouldReturnEmptyAttributesForEmptyUserUpdateRequest() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "user2@example.com",
                "User Two Updated",
                "newpassword",
                null,
                null);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, userUpdateRequest);

        assertThat(attributes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should return empty attributes for unsupported request type")
    void shouldReturnEmptyAttributesForUnsupportedRequestType() throws Exception {
        Object unsupportedRequest = new Object();

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, unsupportedRequest);

        assertThat(attributes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should build attributes for UserCreateRequest with phone and address")
    void shouldBuildAttributesWithPhoneAndAddressForCreateRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user1", "user1@example.com", "User One", "password", "123 Test Street", "1234567890", true);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes).containsEntry("address", "123 Test Street")
                .containsEntry("phone", "1234567890");
    }

    @Test
    @DisplayName("Should build attributes for UserCreateRequest without phone")
    void shouldBuildAttributesWithoutPhoneForCreateRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user1", "user1@example.com", "User One", "password", "123 Test Street", null, true);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes).containsEntry("address", "123 Test Street")
                .doesNotContainKey("phone");
    }

    @Test
    @DisplayName("Should return empty attributes for null fields in UserCreateRequest")
    void shouldReturnEmptyAttributesForNullFieldsInCreateRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(
                "user1", "user1@example.com", "User One", "password", null, null, true);

        Method method = UserServiceImpl.class.getDeclaredMethod("buildAttributes", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> attributes = (Map<String, String>) method.invoke(userService, createRequest);

        assertThat(attributes).isEmpty();
    }

}
