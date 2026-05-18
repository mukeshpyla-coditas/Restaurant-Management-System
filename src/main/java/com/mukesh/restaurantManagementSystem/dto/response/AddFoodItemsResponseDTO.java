package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddFoodItemsResponseDTO {
    private String foodItemName;
    private String foodItemCategory;
    private String message;
}
