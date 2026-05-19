package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ViewReportsResponseDTO {
    private Long branchId;
    private Double totalIncome;
    private Double totalExpenditure;
    private Integer totalOrders;
    private Double totalProfit;
    private Double profitPercentage;
}
