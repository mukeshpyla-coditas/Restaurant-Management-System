package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BillsRepository extends JpaRepository<Bills, Long> {
    @Query("""
        SELECT COALESCE(SUM(b.totalBillAmount), 0)
        FROM Bills b 
        WHERE b.order.restaurantTable.branch.id = :branchId
        AND b.order.placedAt = :date
    """)
    Double getDailyIncome(Long branchId, LocalDate date);

    @Query("""
        SELECT COUNT(b)
        FROM Bills b
        WHERE b.order.restaurantTable.branch.id = :branchId
        AND b.order.placedAt = :date
    """)
    Integer getTotalOrders(Long branchId, LocalDate date);
}
