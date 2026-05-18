package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RestaurantOwnerRegisterResponseDTO {
    private String ownerName;
    private String message;
}
