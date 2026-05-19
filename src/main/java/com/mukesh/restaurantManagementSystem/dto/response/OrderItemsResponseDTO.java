package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Builder
public class OrderItemsResponseDTO {
    private String customerName;
    private Integer tableNumber;
    private Map<String, Integer> orderedItems;
}
