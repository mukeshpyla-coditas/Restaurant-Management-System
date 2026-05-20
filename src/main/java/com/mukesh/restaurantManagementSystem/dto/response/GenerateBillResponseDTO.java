package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GenerateBillResponseDTO {
    private Double ordersAmount;
    private Double taxPercentage;
    private Double totalAmount;
    private String generatedBy;
}
