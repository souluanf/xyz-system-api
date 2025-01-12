package dev.luanfernandes.controller;

import static dev.luanfernandes.domain.constants.PathConstants.USER_V1;
import static dev.luanfernandes.domain.constants.PathConstants.USER_V1_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.config.security.WebSecurityConfig;
import dev.luanfernandes.config.web.ExceptionHandlerAdvice;
import dev.luanfernandes.controller.impl.UserControllerImpl;
import dev.luanfernandes.domain.request.UserCreateRequest;
import dev.luanfernandes.domain.request.UserUpdateRequest;
import dev.luanfernandes.domain.response.UserResponse;
import dev.luanfernandes.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(
        classes = {
            UserControllerImpl.class,
            ExceptionHandlerAdvice.class,
            WebSecurityConfig.class,
        })
@WithMockUser(roles = {"ADMIN"})
@DisplayName("Tests for UserController")
class UserControllerTest {

    static final String TOKEN = "Bearer token";

    MockMvc mvc;

    @MockitoBean
    UserService userService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Should create a new user")
    void shouldCreateNewUser() throws Exception {
        UserCreateRequest userRequest = new UserCreateRequest(
                "john_doe", "john@example.com", "John Doe", "password", "123 Main St", "1234567890", false);

        mvc.perform(post(USER_V1)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).create(userRequest, TOKEN);
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() throws Exception {
        List<UserResponse> users = List.of(
                new UserResponse("john_doe", "john@example.com", "John Doe", "123 Main St", "1234567890", "1"),
                new UserResponse("jane_doe", "jane@example.com", "Jane Doe", "456 Elm St", "9876543210", "2"));

        when(userService.findAll(TOKEN)).thenReturn(users);

        mvc.perform(get(USER_V1).contentType(APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[1].username").value("jane_doe"));

        verify(userService, times(1)).findAll(TOKEN);
    }

    @Test
    @DisplayName("Should find a user by username")
    void shouldFindUserByUsername() throws Exception {
        String username = "john_doe";
        UserResponse user =
                new UserResponse(username, "john@example.com", "John Doe", "123 Main St", "1234567890", "1");

        when(userService.findByUsername(username, TOKEN)).thenReturn(user);

        mvc.perform(get(USER_V1_ID, username).contentType(APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));

        verify(userService, times(1)).findByUsername(username, TOKEN);
    }

    @Test
    @DisplayName("Should update a user")
    void shouldUpdateUser() throws Exception {
        String username = "john_doe";
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .name("John Updated")
                .email("updated@example.com")
                .password("password")
                .address("456 Updated St")
                .phone("9876543210")
                .build();

        mvc.perform(put(USER_V1_ID, username)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(userUpdateRequest)))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).update(username, userUpdateRequest, TOKEN);
    }

    @Test
    @DisplayName("Should delete a user by username")
    void shouldDeleteUserByUsername() throws Exception {
        String username = "john_doe";

        mvc.perform(delete(USER_V1_ID, username).contentType(APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, TOKEN))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(username, TOKEN);
    }
}
