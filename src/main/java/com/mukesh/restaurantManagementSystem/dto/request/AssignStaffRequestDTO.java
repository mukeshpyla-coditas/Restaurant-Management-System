package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AssignStaffRequestDTO {
    @NotNull
    private Long waiterStaffId;

    @NotNull
    private List<Long> tableNumbers;
}
