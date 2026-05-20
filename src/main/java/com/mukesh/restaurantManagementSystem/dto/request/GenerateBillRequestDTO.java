package com.mukesh.restaurantManagementSystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenerateBillRequestDTO {
    private Long customerId;
    private Integer tableNumber;
    private Double discount;
}

