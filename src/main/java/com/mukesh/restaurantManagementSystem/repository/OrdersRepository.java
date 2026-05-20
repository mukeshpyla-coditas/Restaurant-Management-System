package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.Customers;
import com.mukesh.restaurantManagementSystem.entity.Orders;
import com.mukesh.restaurantManagementSystem.entity.RestaurantTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByCustomerAndRestaurantTable(Customers customer, RestaurantTables restaurantTable);

    @Query("""
        SELECT COALESCE(SUM(o.finalAmountIncludingTax), 0)
        FROM Orders o
        WHERE o.restaurantTable.branch.id = :branchId
        AND o.placedAt = :date
    """)
    Double getDailyIncome(Long branchId, LocalDate date);

    @Query("""
        SELECT COUNT(o)
        FROM Orders o
        WHERE o.restaurantTable.branch.id = :branchId
        AND o.placedAt = :date
    """)
    Integer getTotalOrders(Long branchId, LocalDate date);
}
