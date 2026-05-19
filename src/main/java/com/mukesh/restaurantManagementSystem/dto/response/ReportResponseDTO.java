package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponseDTO {
    private Double totalIncome;
    private Double totalExpenditure;
    private Integer totalOrders;
    private Double totalProfit;
}
