package dev.luanfernandes.service.impl;

import dev.luanfernandes.domain.entity.Salesperson;
import dev.luanfernandes.domain.mapper.SalesPersonMapper;
import dev.luanfernandes.domain.response.SalespersonResponse;
import dev.luanfernandes.domain.response.TotalSalesResponse;
import dev.luanfernandes.repository.OrderRepository;
import dev.luanfernandes.repository.SalespersonRepository;
import dev.luanfernandes.service.SalespersonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalespersonServiceImpl implements SalespersonService {
    private final SalespersonRepository salespersonRepository;
    private final OrderRepository orderRepository;
    private final SalesPersonMapper salesPersonMapper;

    @Transactional
    @Override
    public void updateSalespersonNames() {
        List<Long> salespersonIdsWithOrders = orderRepository.findSalespeopleWithTwoOrMoreOrders();
        List<Salesperson> salespeople = salespersonRepository.findAll();
        for (Salesperson salesperson : salespeople) {
            if (salespersonIdsWithOrders.contains(salesperson.getId())) {
                if (!salesperson.getName().endsWith("*")) {
                    salesperson.setName(salesperson.getName() + "*");
                }
            } else {
                if (salesperson.getName().endsWith("*")) {
                    salesperson.setName(salesperson
                            .getName()
                            .substring(0, salesperson.getName().length() - 1));
                }
            }
        }
        salespersonRepository.saveAll(salespeople);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SalespersonResponse> getSalespersonWithMoreThanOneOrder() {
        List<Long> salespersonIdsWithOrders = orderRepository.findSalespeopleWithTwoOrMoreOrders();
        return salespersonRepository.findAll().stream()
                .filter(salesperson -> salespersonIdsWithOrders.contains(salesperson.getId()))
                .map(salesPersonMapper::map)
                .toList();
    }

    @Transactional
    @Override
    public void deleteSalespeopleByCity(String city) {
        List<Long> salespersonIds = orderRepository.findSalespeopleByCity(city);
        salespersonRepository.deleteAllById(salespersonIds);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findSalespeopleWithoutOrdersWithCustomer(String customerName) {
        return salespersonRepository.findSalespeopleWithoutOrdersWithCustomer(customerName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TotalSalesResponse> findTotalSalesBySalesperson() {
        return salespersonRepository.findTotalSalesBySalesperson();
    }
}
