package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Builder
public class CancelOrderItemResponseDTO {
    private Map<String, Integer> remainingOrders;
    private String message;
}
