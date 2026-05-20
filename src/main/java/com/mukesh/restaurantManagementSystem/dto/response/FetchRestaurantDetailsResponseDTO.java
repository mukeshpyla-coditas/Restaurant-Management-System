package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class FetchRestaurantDetailsResponseDTO {
    private Long restaurantId;
    private String restaurantName;
    private LocalDate restaurantCreatedAt;
    private String ownerName;
    private List<FetchBranchDetailsResponseDTO> branchDetails;
}
