package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BranchRegisterResponseDTO {
    private String restaurantOwner;
    private String restaurantChainName;
    private String branchName;
    private String message;
}
