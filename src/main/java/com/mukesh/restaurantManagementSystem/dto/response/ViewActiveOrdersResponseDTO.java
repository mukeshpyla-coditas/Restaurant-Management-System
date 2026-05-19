package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ViewActiveOrdersResponseDTO {
    private String itemName;
    private String ingredients;
    private String preparationNotes;
    private String orderStatus;
}
