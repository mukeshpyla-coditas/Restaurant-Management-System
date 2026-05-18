package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BranchManagerInviteResponseDTO {
    private String sentBy;
    private String sentTo;
    private String message;
}
