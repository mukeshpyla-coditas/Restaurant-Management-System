package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ViewMenuResponseDTO {
    private String itemName;
    private String itemDescription;
    private String ingredients;
    private Double calories;
    private Double price;
}
