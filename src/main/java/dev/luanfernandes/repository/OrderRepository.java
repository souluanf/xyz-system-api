package dev.luanfernandes.repository;

import dev.luanfernandes.domain.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o.salesperson.id FROM Order o GROUP BY o.salesperson.id HAVING COUNT(o.id) >= 2")
    List<Long> findSalespeopleWithTwoOrMoreOrders();

    @Query("SELECT DISTINCT o.salesperson.id FROM Order o JOIN o.customer c WHERE c.city = :city")
    List<Long> findSalespeopleByCity(@Param("city") String city);
}
