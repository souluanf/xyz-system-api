package dev.luanfernandes.service;

import dev.luanfernandes.domain.response.SalespersonResponse;
import dev.luanfernandes.domain.response.TotalSalesResponse;
import java.util.List;

public interface SalespersonService {
    void updateSalespersonNames();

    List<SalespersonResponse> getSalespersonWithMoreThanOneOrder();

    void deleteSalespeopleByCity(String city);

    List<String> findSalespeopleWithoutOrdersWithCustomer(String customerName);

    List<TotalSalesResponse> findTotalSalesBySalesperson();
}
