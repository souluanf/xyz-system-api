package dev.luanfernandes.controller;

import static dev.luanfernandes.domain.constants.PathConstants.PLANTS_V1;
import static dev.luanfernandes.domain.constants.PathConstants.PLANTS_V1_ID;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.config.security.WebSecurityConfig;
import dev.luanfernandes.config.web.ExceptionHandlerAdvice;
import dev.luanfernandes.controller.impl.PlantControllerImpl;
import dev.luanfernandes.domain.request.PlantCreateRequest;
import dev.luanfernandes.domain.request.PlantUpdateRequest;
import dev.luanfernandes.domain.response.PlantResponse;
import dev.luanfernandes.service.PlantService;
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
            PlantControllerImpl.class,
            ExceptionHandlerAdvice.class,
            WebSecurityConfig.class,
        })
@WithMockUser(roles = {"ADMIN"})
@DisplayName("Tests for PlantController")
class PlantControllerTest {

    MockMvc mvc;

    @MockitoBean
    PlantService plantService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Should create a new plant")
    void shouldCreatePlant() throws Exception {
        var createRequest = new PlantCreateRequest(3L, "Red flower");
        var plantResponse = new PlantResponse(3L, "Red flower");

        when(plantService.createPlant(createRequest)).thenReturn(plantResponse);

        mvc.perform(post(PLANTS_V1)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(plantResponse.code()))
                .andExpect(jsonPath("$.description").value(plantResponse.description()));
        verify(plantService, times(1)).createPlant(createRequest);
    }

    @Test
    @DisplayName("Should get all plants")
    void shouldGetAllPlants() throws Exception {
        var plantResponses = List.of(new PlantResponse(1L, "Red flower"), new PlantResponse(2L, "Yellow flower"));

        when(plantService.getAll()).thenReturn(plantResponses);

        mvc.perform(get(PLANTS_V1).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value(1L))
                .andExpect(jsonPath("$[0].description").value("Red flower"))
                .andExpect(jsonPath("$[1].code").value(2L))
                .andExpect(jsonPath("$[1].description").value("Yellow flower"));

        verify(plantService, times(1)).getAll();
    }

    @Test
    @DisplayName("Should get a plant by ID")
    void shouldGetPlantById() throws Exception {
        var plantResponse = new PlantResponse(1L, "Red flower");

        when(plantService.getById(1L)).thenReturn(plantResponse);

        mvc.perform(get(PLANTS_V1_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(plantResponse.code()))
                .andExpect(jsonPath("$.description").value(plantResponse.description()));

        verify(plantService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should update a plant by ID")
    void shouldUpdatePlantById() throws Exception {
        var updateRequest = new PlantUpdateRequest(1L, "Red flower updated");
        doNothing().when(plantService).updatePlant(eq(1L), any(PlantUpdateRequest.class));
        mvc.perform(put(PLANTS_V1_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());
        verify(plantService, times(1)).updatePlant(eq(1L), any(PlantUpdateRequest.class));
    }

    @Test
    @DisplayName("Should delete a plant by ID")
    void shouldDeletePlantById() throws Exception {
        mvc.perform(delete(PLANTS_V1_ID, 1L).contentType(APPLICATION_JSON)).andExpect(status().isNoContent());

        verify(plantService, times(1)).deleteById(1L);
    }
}
