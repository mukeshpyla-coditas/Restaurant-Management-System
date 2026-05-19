package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.PurchaseBills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PurchaseBillsRepository extends JpaRepository<PurchaseBills, Long> {
    @Query("""
        SELECT COALESCE(SUM(p.totalPurchaseAmount), 0)
        FROM PurchaseBills p
        WHERE p.stockManagementStaff.branch.id = :branchId
        AND p.purchaseDate = :date
    """)
    Double getDailyExpenditure(Long branchId, LocalDate date);
}
