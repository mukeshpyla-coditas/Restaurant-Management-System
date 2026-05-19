package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class PurchaseBillUploadResponseDTO {
    private Double totalPurchaseAmount;
    private String uploadedBy;
    private LocalDate uploadedAt;
}
