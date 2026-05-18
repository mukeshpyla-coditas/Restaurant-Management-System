package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BranchManagerInviteRequestDTO {
    @NotNull
    private Long senderId;
    @NotNull
    private Long restaurantId;
    @NotNull
    private Long branchId;
    @Email
    private String receiverMail;
    @NotNull
    private Double salary;
    @NotNull
    private String message;
}
