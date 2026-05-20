package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PrepareOrderResponseDTO {
    private Integer tableNumber;
    private Long orderItemId;
    private String cookingStaffName;
    private String message;
}
