package dev.luanfernandes.controller;

import static dev.luanfernandes.domain.constants.PathConstants.ANAGRAMS_V1;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.config.security.WebSecurityConfig;
import dev.luanfernandes.config.web.ExceptionHandlerAdvice;
import dev.luanfernandes.controller.impl.AnagramControllerImpl;
import dev.luanfernandes.domain.request.AnagramRequest;
import dev.luanfernandes.domain.response.AnagramResponse;
import dev.luanfernandes.service.AnagramService;
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
            AnagramControllerImpl.class,
            ExceptionHandlerAdvice.class,
            WebSecurityConfig.class,
        })
@WithMockUser(roles = {"ADMIN"})
@DisplayName("Tests for AnagramController")
class AnagramControllerTest {

    MockMvc mvc;

    @MockitoBean
    AnagramService anagramService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Should return anagrams for a valid input with input and totalAnagrams")
    void shouldReturnAnagramsForValidInput() throws Exception {
        String input = "abc";
        List<String> anagrams = List.of("abc", "acb", "bac", "bca", "cab", "cba");
        AnagramResponse response = new AnagramResponse(input, anagrams.size(), anagrams);

        when(anagramService.generateAnagrams(new AnagramRequest(input))).thenReturn(response);

        mvc.perform(post(ANAGRAMS_V1)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .content(new ObjectMapper().writeValueAsString(new AnagramRequest(input))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.input").value(input))
                .andExpect(jsonPath("$.totalAnagrams").value(anagrams.size()))
                .andExpect(jsonPath("$.anagrams").isArray())
                .andExpect(jsonPath("$.anagrams[0]").value("abc"))
                .andExpect(jsonPath("$.anagrams[1]").value("acb"));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST for invalid input")
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        String invalidInput = "abc123";
        AnagramRequest request = new AnagramRequest(invalidInput);

        mvc.perform(post(ANAGRAMS_V1)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.detail").value("Validation failed for argument"));
    }

    @Test
    @DisplayName("Should return 422 UNPROCESSABLE ENTITY for empty input")
    void shouldReturnUnprocessableEntityForEmptyInput() throws Exception {
        String emptyInput = "";
        AnagramRequest request = new AnagramRequest(emptyInput);

        mvc.perform(post(ANAGRAMS_V1)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.detail").value("Validation failed for argument"));
    }
}
