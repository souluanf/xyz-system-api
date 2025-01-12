package dev.luanfernandes.controller.impl;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import dev.luanfernandes.controller.SalespersonController;
import dev.luanfernandes.domain.response.SalespersonResponse;
import dev.luanfernandes.domain.response.TotalSalesResponse;
import dev.luanfernandes.service.SalespersonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SalespersonControllerImpl implements SalespersonController {

    private final SalespersonService salespersonService;

    @Override
    public ResponseEntity<List<String>> getSalespeopleWithoutOrders(String customerName) {
        return ok(salespersonService.findSalespeopleWithoutOrdersWithCustomer(customerName));
    }

    @Override
    public ResponseEntity<List<SalespersonResponse>> getSalespersonWithMoreThanOneOrder() {
        return ok(salespersonService.getSalespersonWithMoreThanOneOrder());
    }

    @Override
    public ResponseEntity<List<TotalSalesResponse>> getTotalSalesBySalesperson() {
        return ok(salespersonService.findTotalSalesBySalesperson());
    }

    @Override
    public ResponseEntity<Void> updateSalespersonNames() {
        salespersonService.updateSalespersonNames();
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteSalespeopleByCity(String city) {
        salespersonService.deleteSalespeopleByCity(city);
        return noContent().build();
    }
}
