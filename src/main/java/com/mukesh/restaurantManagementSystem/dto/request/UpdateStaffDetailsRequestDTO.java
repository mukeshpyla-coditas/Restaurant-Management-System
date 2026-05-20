package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStaffDetailsRequestDTO {
    @NotNull
    private Long staffId;
    @NotNull
    private String fullName;
    @NotNull
    private String username;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String gender;
    @NotNull
    private String aadharNumber;
    @NotNull
    private String contactNumber;
    @NotNull
    private String photoUrl;
    @Pattern(regexp = "WAITER_STAFF|COOKING_STAFF|STOCK_MANAGEMENT_STAFF", message = "Please select the valid staffType.")
    private String staffType;
}
