package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddItemsToExistingOrderRequestDTO {
    @NotNull
    private Long orderId;
    @NotNull
    private Long foodItemId;
    @NotNull
    private Integer quantity;
}
