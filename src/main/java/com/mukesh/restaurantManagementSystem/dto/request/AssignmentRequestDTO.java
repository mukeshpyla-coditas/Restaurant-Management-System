package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AssignmentRequestDTO {
    @NotNull
    private List<Integer> selectedTables;
    @NotNull
    private Long waiterId;
    @NotNull
    private boolean temporaryAssignment;
}
