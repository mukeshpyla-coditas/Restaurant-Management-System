package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddItemsToExistingOrderResponseDTO {
    private Long foodItemId;
    private String foodItemName;
    private Long orderId;
}
