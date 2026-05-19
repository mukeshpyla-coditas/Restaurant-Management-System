package com.mukesh.restaurantManagementSystem.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AssignmentRequestDTO {
    private List<Long> selectedTables;
    private Long waiterId;
    private boolean temporaryAssignment;
}
