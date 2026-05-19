package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
public class AssignmentResponseDTO {
    private Map<Long, List<Integer>> assignedTables;
    private boolean temporaryAssignment;
    private String message;
}
