package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ViewReportsRequestDTO {
    @NotNull
    private Long branchId;
    @NotNull
    private LocalDate from;
    @NotNull
    private LocalDate to;
}
