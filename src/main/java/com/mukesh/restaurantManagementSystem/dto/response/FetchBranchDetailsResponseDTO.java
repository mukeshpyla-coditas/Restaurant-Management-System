package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class FetchBranchDetailsResponseDTO {
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private Long seatingCapacity;
    private LocalDate createdAt;
    private String contactNumber;
    private Long managerId;
    private String managerName;
    private List<FetchStaffDetailsResponseDTO> staffDetails;
}
