package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MenuCreationResponseDTO {
    private Long branchId;
    private String createdBy;
    private String message;
}
