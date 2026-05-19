package com.mukesh.restaurantManagementSystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddItemsToExistingOrderRequestDTO {
    private Long orderId;
    private Long foodItemId;
    private Integer quantity;
}
