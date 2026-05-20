package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FetchStaffDetailsResponseDTO {
    private Long staffId;
    private String staffName;
    private String staffType;
    private boolean isPresent;
}
