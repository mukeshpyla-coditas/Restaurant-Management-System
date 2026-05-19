package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.dto.response.ReportResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.DailyReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReports, Long> {

    @Query("""
        SELECT COALESCE(SUM(b.totalIncome), 0) AS totalIncome,
        COALESCE(SUM(b.totalExpenditure), 0) AS totalExpenditure,
        COALESCE(SUM(b.totalOrders), 0) AS totalOrders,
        COALESCE(SUM(b.totalProfit), 0) AS totalProfit
        FROM DailyReports b WHERE b.branchId = :branchId AND
        b.reportDate BETWEEN :from AND :to
    """)
    ReportResponseDTO findReportsBetweenFromAndTo(Long branchId, LocalDate from, LocalDate to);
}
