package dev.luanfernandes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import dev.luanfernandes.domain.mapper.KeycloakMapper;
import dev.luanfernandes.domain.request.TokenRequest;
import dev.luanfernandes.domain.response.TokenResponse;
import dev.luanfernandes.service.impl.AuthServiceImpl;
import dev.luanfernandes.webclient.KeycloakClient;
import dev.luanfernandes.webclient.request.KeyCloakTokenRequest;
import dev.luanfernandes.webclient.response.KeyCloakTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private KeycloakClient webClient;

    @Mock
    private KeycloakMapper keycloakMapper;

    private TokenRequest tokenRequest;
    private KeyCloakTokenRequest keyCloakTokenRequest;
    private KeyCloakTokenResponse keyCloakTokenResponse;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        tokenRequest =
                TokenRequest.builder().username("username").password("password").build();

        keyCloakTokenRequest = KeyCloakTokenRequest.builder()
                .username("username")
                .password("password")
                .build();

        keyCloakTokenResponse = KeyCloakTokenResponse.builder()
                .accessToken("access_token")
                .expiresIn(3600)
                .refreshToken("refresh_token")
                .refreshExpiresIn(7200)
                .tokenType("bearer")
                .build();

        tokenResponse = TokenResponse.builder()
                .accessToken("access_token")
                .expiresIn(3600)
                .refreshToken("refresh_token")
                .refreshExpiresIn(7200)
                .tokenType("bearer")
                .build();
    }

    @Test
    @DisplayName("Should return a TokenResponse when tokenRequest is valid")
    void shouldReturnTokenResponseWhenTokenRequestIsValid() {
        when(keycloakMapper.map(tokenRequest)).thenReturn(keyCloakTokenRequest);
        when(webClient.getToken(keyCloakTokenRequest)).thenReturn(keyCloakTokenResponse);
        when(keycloakMapper.map(keyCloakTokenResponse)).thenReturn(tokenResponse);
        TokenResponse result = authService.getToken(tokenRequest);

        assertThat(result).isEqualTo(tokenResponse);
        verify(keycloakMapper, times(1)).map(tokenRequest);
        verify(webClient, times(1)).getToken(keyCloakTokenRequest);
        verify(keycloakMapper, times(1)).map(keyCloakTokenResponse);
    }

    @Test
    @DisplayName("Should throw an exception when webClient returns an error")
    void shouldThrowExceptionWhenWebClientFails() {
        when(keycloakMapper.map(tokenRequest)).thenReturn(keyCloakTokenRequest);
        when(webClient.getToken(keyCloakTokenRequest)).thenThrow(new RuntimeException("Error while fetching token"));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            authService.getToken(tokenRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Error while fetching token");
        verify(keycloakMapper, times(1)).map(tokenRequest);
        verify(webClient, times(1)).getToken(keyCloakTokenRequest);
        verifyNoMoreInteractions(keycloakMapper);
    }
}
