package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RestaurantRegisterResponseDTO {
    private String ownerName;
    private String restaurantName;
    private String message;
}
