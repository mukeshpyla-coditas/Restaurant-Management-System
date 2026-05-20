package com.mukesh.restaurantManagementSystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrepareOrderRequestDTO {
    private Long orderId;
    private Long orderItemId;
}
