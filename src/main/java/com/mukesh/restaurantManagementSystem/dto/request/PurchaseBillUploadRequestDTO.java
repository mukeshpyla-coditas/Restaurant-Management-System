package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PurchaseBillUploadRequestDTO {
    @NotNull
    private Double totalPurchaseAmount;
    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    private String billUrl;
}
