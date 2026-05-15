package com.mukesh.restaurantManagementSystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private Integer httpStatus;
    private String errorMessage;
    private LocalDate timestamp;
}
