package dev.luanfernandes.controller;

import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_DELETE_BY_CITY;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_MORE_THAN_ONE_ORDER;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_TOTAL_SALES;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_UPDATE_NAMES;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_WITHOUT_ORDERS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import dev.luanfernandes.config.security.WebSecurityConfig;
import dev.luanfernandes.config.web.ExceptionHandlerAdvice;
import dev.luanfernandes.controller.impl.SalespersonControllerImpl;
import dev.luanfernandes.domain.response.SalespersonResponse;
import dev.luanfernandes.domain.response.TotalSalesResponse;
import dev.luanfernandes.service.SalespersonService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
            SalespersonControllerImpl.class,
            ExceptionHandlerAdvice.class,
            WebSecurityConfig.class,
        })
@WithMockUser(roles = {"ADMIN"})
@DisplayName("Tests for SalespersonController")
class SalespersonControllerTest {

    MockMvc mvc;

    @MockitoBean
    SalespersonService salespersonService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Should return salespeople without orders by customer")
    void shouldReturnSalespeopleWithoutOrdersByCustomer() throws Exception {
        String customerName = "Test Customer";
        List<String> salespeople = List.of("John", "Jane");

        when(salespersonService.findSalespeopleWithoutOrdersWithCustomer(customerName))
                .thenReturn(salespeople);

        mvc.perform(get(SALES_PERSON_V1_WITHOUT_ORDERS)
                        .param("customerName", customerName)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("John"))
                .andExpect(jsonPath("$[1]").value("Jane"));

        verify(salespersonService, times(1)).findSalespeopleWithoutOrdersWithCustomer(customerName);
    }

    @Test
    @DisplayName("Should return salespeople with more than one order")
    void shouldReturnSalespeopleWithMoreThanOneOrder() throws Exception {
        List<SalespersonResponse> salespeople =
                List.of(new SalespersonResponse("John", 30, 5000.0), new SalespersonResponse("Jane", 28, 4500.0));

        when(salespersonService.getSalespersonWithMoreThanOneOrder()).thenReturn(salespeople);

        mvc.perform(get(SALES_PERSON_V1_MORE_THAN_ONE_ORDER).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[1].age").value(28));

        verify(salespersonService, times(1)).getSalespersonWithMoreThanOneOrder();
    }

    @Test
    @DisplayName("Should return total sales by salesperson")
    void shouldReturnTotalSalesBySalesperson() throws Exception {
        List<TotalSalesResponse> totalSales =
                List.of(new TotalSalesResponse(1L, "John", 15000.0), new TotalSalesResponse(2L, "Jane", 12000.0));

        when(salespersonService.findTotalSalesBySalesperson()).thenReturn(totalSales);

        mvc.perform(get(SALES_PERSON_V1_TOTAL_SALES).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].totalSales").value(15000.0))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[1].totalSales").value(12000.0));

        verify(salespersonService, times(1)).findTotalSalesBySalesperson();
    }

    @Test
    @DisplayName("Should update salesperson names")
    void shouldUpdateSalespersonNames() throws Exception {
        mvc.perform(put(SALES_PERSON_V1_UPDATE_NAMES).contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(salespersonService, times(1)).updateSalespersonNames();
    }

    @Test
    @DisplayName("Should delete salespeople by city")
    void shouldDeleteSalespeopleByCity() throws Exception {
        String city = "New York";

        mvc.perform(delete(SALES_PERSON_V1_DELETE_BY_CITY).param("city", city).contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(salespersonService, times(1)).deleteSalespeopleByCity(city);
    }
}
