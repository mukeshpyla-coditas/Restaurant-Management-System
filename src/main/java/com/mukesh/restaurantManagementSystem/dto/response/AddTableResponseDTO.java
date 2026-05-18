package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddTableResponseDTO {
    private Integer tableNumber;
    private String branchName;
    private String addedBy;
}
