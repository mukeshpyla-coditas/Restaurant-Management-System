package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddStaffResponseDTO {
    private Long staffId;
    private String staffName;
    private String staffType;
    private String managerName;
    private String branchName;
}
