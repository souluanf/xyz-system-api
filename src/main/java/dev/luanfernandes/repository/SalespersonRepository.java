package dev.luanfernandes.repository;

import dev.luanfernandes.domain.entity.Salesperson;
import dev.luanfernandes.domain.response.TotalSalesResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalespersonRepository extends JpaRepository<Salesperson, Long> {

    @Query("SELECT s.name FROM Salesperson s " + "WHERE NOT EXISTS (SELECT 1 FROM Order o "
            + "JOIN o.customer c WHERE c.name = :customerName AND o.salesperson.id = s.id)")
    List<String> findSalespeopleWithoutOrdersWithCustomer(@Param("customerName") String customerName);

    @Query("SELECT new dev.luanfernandes.domain.response.TotalSalesResponse(s.id, s.name, COALESCE(SUM(o.amount), 0)) "
            + "FROM Salesperson s LEFT JOIN Order o ON s.id = o.salesperson.id "
            + "GROUP BY s.id, s.name ORDER BY s.name")
    List<TotalSalesResponse> findTotalSalesBySalesperson();
}
