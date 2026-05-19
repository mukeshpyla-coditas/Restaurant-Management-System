package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class OrderItemsRequestDTO {
    @NotNull
    private String customerName;
    @NotNull
    @Size(min = 10, max = 10, message = "ContactNumber must be of 10 digits")
    private String contactNumber;
    @NotNull
    Map<Long, Integer> foodItems;
    @NotNull
    private Long tableNumber;
}
