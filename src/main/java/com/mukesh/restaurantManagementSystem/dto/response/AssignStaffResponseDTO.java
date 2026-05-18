package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class AssignStaffResponseDTO {
    private Long staffId;
    private String staffName;
    private String assignedBy;
    private List<Long> tablesAssigned;
}
