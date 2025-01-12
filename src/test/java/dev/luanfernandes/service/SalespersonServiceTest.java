package dev.luanfernandes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.domain.entity.Salesperson;
import dev.luanfernandes.domain.mapper.SalesPersonMapper;
import dev.luanfernandes.domain.response.SalespersonResponse;
import dev.luanfernandes.domain.response.TotalSalesResponse;
import dev.luanfernandes.repository.OrderRepository;
import dev.luanfernandes.repository.SalespersonRepository;
import dev.luanfernandes.service.impl.SalespersonServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SalespersonServiceTest {

    @Mock
    SalespersonRepository salespersonRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    SalespersonServiceImpl salespersonService;

    @Mock
    SalesPersonMapper salesPersonMapper;

    @Test
    void updateSalespersonNames_ShouldUpdateCorrectly() {
        List<Long> salespersonIdsWithOrders = List.of(1L, 2L);

        Salesperson salesperson1 =
                Salesperson.builder().name("John").age(30).salary(5000.0).build();
        salesperson1.setId(1L);
        Salesperson salesperson2 =
                Salesperson.builder().name("Jane").age(28).salary(4500.0).build();
        salesperson2.setId(2L);
        Salesperson salesperson3 =
                Salesperson.builder().name("Doe*").age(35).salary(6000.0).build();
        salesperson3.setId(3L);

        List<Salesperson> salespeople = List.of(salesperson1, salesperson2, salesperson3);

        when(orderRepository.findSalespeopleWithTwoOrMoreOrders()).thenReturn(salespersonIdsWithOrders);
        when(salespersonRepository.findAll()).thenReturn(salespeople);
        salespersonService.updateSalespersonNames();

        verify(salespersonRepository, times(1)).saveAll(salespeople);
        assertThat(salespeople.get(0).getName()).isEqualTo("John*");
        assertThat(salespeople.get(1).getName()).isEqualTo("Jane*");
        assertThat(salespeople.get(2).getName()).isEqualTo("Doe");
    }

    @Test
    void deleteSalespeopleByCity_ShouldDeleteCorrectly() {
        String city = "New York";
        List<Long> salespersonIds = List.of(1L, 2L);

        when(orderRepository.findSalespeopleByCity(city)).thenReturn(salespersonIds);

        salespersonService.deleteSalespeopleByCity(city);

        verify(salespersonRepository, times(1)).deleteAllById(salespersonIds);
    }

    @Test
    void findSalespeopleWithoutOrdersWithCustomer_ShouldReturnCorrectNames() {
        String customerName = "Samsonic";
        List<String> expectedNames = List.of("John", "Jane");

        when(salespersonRepository.findSalespeopleWithoutOrdersWithCustomer(customerName))
                .thenReturn(expectedNames);

        List<String> result = salespersonService.findSalespeopleWithoutOrdersWithCustomer(customerName);

        assertThat(result).containsExactlyInAnyOrder("John", "Jane");
    }

    @Test
    void findTotalSalesBySalesperson_ShouldReturnCorrectTotalSales() {
        List<TotalSalesResponse> expectedResponse =
                List.of(new TotalSalesResponse(1L, "John", 15000.0), new TotalSalesResponse(2L, "Jane", 12000.0));

        when(salespersonRepository.findTotalSalesBySalesperson()).thenReturn(expectedResponse);

        List<TotalSalesResponse> result = salespersonService.findTotalSalesBySalesperson();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("John");
        assertThat(result.get(0).totalSales()).isEqualTo(15000.0);
        assertThat(result.get(1).name()).isEqualTo("Jane");
        assertThat(result.get(1).totalSales()).isEqualTo(12000.0);
    }

    @Test
    void getSalespersonWithMoreThanOneOrder_ShouldReturnCorrectSalespeople() {
        List<Long> salespersonIdsWithOrders = List.of(1L, 2L);
        Salesperson salesperson1 =
                Salesperson.builder().name("John").age(30).salary(5000.0).build();
        salesperson1.setId(1L);
        Salesperson salesperson2 =
                Salesperson.builder().name("Jane").age(28).salary(4500.0).build();
        salesperson2.setId(2L);
        Salesperson salesperson3 =
                Salesperson.builder().name("Doe").age(35).salary(6000.0).build();
        salesperson3.setId(3L);

        List<Salesperson> salespeople = List.of(salesperson1, salesperson2, salesperson3);
        List<SalespersonResponse> expectedResponse =
                List.of(new SalespersonResponse("John", 30, 5000.0), new SalespersonResponse("Jane", 28, 4500.0));

        when(orderRepository.findSalespeopleWithTwoOrMoreOrders()).thenReturn(salespersonIdsWithOrders);
        when(salespersonRepository.findAll()).thenReturn(salespeople);
        when(salesPersonMapper.map(salesperson1)).thenReturn(expectedResponse.get(0));
        when(salesPersonMapper.map(salesperson2)).thenReturn(expectedResponse.get(1));

        List<SalespersonResponse> result = salespersonService.getSalespersonWithMoreThanOneOrder();
        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResponse);
    }

    @Test
    void getSalespersonWithMoreThanOneOrder_ShouldReturnEmptyListWhenNoSalespeople() {
        List<Long> salespersonIdsWithOrders = List.of(1L, 2L);
        List<Salesperson> salespeople = List.of();

        when(orderRepository.findSalespeopleWithTwoOrMoreOrders()).thenReturn(salespersonIdsWithOrders);
        when(salespersonRepository.findAll()).thenReturn(salespeople);

        List<SalespersonResponse> result = salespersonService.getSalespersonWithMoreThanOneOrder();

        assertThat(result).isEmpty();
    }
}
