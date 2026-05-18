package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ManagerRegisterResponseDTO {
    private Long managerId;
    private String managerName;
    private String branchName;
    private String restaurantName;
    private String restaurantOwnerName;
}
