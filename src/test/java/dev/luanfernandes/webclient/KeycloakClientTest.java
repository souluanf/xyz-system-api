package dev.luanfernandes.webclient;

import dev.luanfernandes.domain.request.Credential;
import dev.luanfernandes.webclient.request.KeyCloakTokenRequest;
import dev.luanfernandes.webclient.request.KeyCloakUserRequest;
import dev.luanfernandes.webclient.request.KeyCloakUserUpdateRequest;
import dev.luanfernandes.webclient.response.KeyCloakTokenResponse;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
class KeycloakClientTest {

    @InjectMocks
    private KeycloakClient keycloakClient;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    private final String accessToken = "Bearer token";


    @BeforeEach
    void setUp() {
        lenient().doReturn(requestBodyUriSpec).when(restClient).post();
        lenient().doReturn(requestBodyUriSpec).when(restClient).put();
        lenient().doReturn(requestHeadersUriSpec).when(restClient).get();
    }
    @Test
    @DisplayName("Should retrieve token successfully")
    void shouldRetrieveTokenSuccessfully() {
        KeyCloakTokenRequest tokenRequest = KeyCloakTokenRequest.builder()
                .username("username")
                .password("password")
                .build();

        KeyCloakTokenResponse tokenResponse = KeyCloakTokenResponse.builder()
                .accessToken(accessToken)
                .expiresIn(300)
                .refreshExpiresIn(1800)
                .refreshToken("refresh_token")
                .tokenType("Bearer")
                .notBeforePolicy(0)
                .sessionState("e3c5e649-4be1-421d-a5bc-20a12a6a4fb2")
                .scope("email profile")
                .build();


        ReflectionTestUtils.setField(keycloakClient, "clientId", "test-client-id");
        ReflectionTestUtils.setField(keycloakClient, "realm", "test-realm");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", tokenRequest.username());
        formData.add("password", tokenRequest.password());
        formData.add("client_id", "test-client-id");
        formData.add("grant_type", "password");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/realms/{realm}/protocol/openid-connect/token", "test-realm"))
                .thenReturn(requestBodySpec);
        when(requestBodySpec.header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE))
                .thenReturn(requestBodySpec);
        when(requestBodySpec.body(formData)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(KeyCloakTokenResponse.class)).thenReturn(tokenResponse);

        KeyCloakTokenResponse actualResponse = keycloakClient.getToken(tokenRequest);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.accessToken()).isEqualTo(accessToken);

        verify(requestBodyUriSpec, times(1)).uri("/realms/{realm}/protocol/openid-connect/token", "test-realm");
        verify(requestBodySpec, times(1)).header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
        verify(requestBodySpec, times(1)).body(formData);
        verify(requestBodySpec, times(1)).retrieve();
        verify(responseSpec, times(1)).body(KeyCloakTokenResponse.class);
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
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
        when(requestBodyUriSpec.uri(anyString(), any(Object[].class))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(KeyCloakUserRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(null);

        keycloakClient.createUser(userRequest, accessToken);

        verify(requestBodyUriSpec).uri(anyString(), any(Object[].class));
        verify(requestBodySpec).header(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        verify(requestBodySpec).header(AUTHORIZATION, accessToken);
        verify(requestBodySpec).body(userRequest);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        KeyCloakUserUpdateRequest userUpdateRequest = KeyCloakUserUpdateRequest.builder()
                .email("testuser@example.com")
                .firstName("Test User")
                .emailVerified(true)
                .credentials(List.of(Credential.builder()
                        .type("password")
                        .value("password123")
                        .build()))
                .attributes(Map.of("address", "123 Test Street", "phone", "1234567890"))
                .build();
        when(requestBodyUriSpec.uri(anyString(), any(Object[].class))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(KeyCloakUserUpdateRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(null);

        keycloakClient.updateUser("user-id", userUpdateRequest, accessToken);

        verify(requestBodyUriSpec).uri(anyString(), any(Object[].class));
        verify(requestBodySpec).header(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        verify(requestBodySpec).header(AUTHORIZATION, accessToken);
        verify(requestBodySpec).body(userUpdateRequest);
    }
}