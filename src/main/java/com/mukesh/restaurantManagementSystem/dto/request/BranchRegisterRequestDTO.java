package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Setter
@Getter
public class BranchRegisterRequestDTO {
    @NotNull
    private Long restaurantChainId;
    @NotNull
    private String branchName;
    @NotNull
    private String address;
    @NotNull
    private Long seatingCapacity;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private String contactNumber;
}
