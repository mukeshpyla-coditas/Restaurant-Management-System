package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStaffDetailsRequestDTO {
    private Long staffId;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String gender;
    private String aadharNumber;
    private String contactNumber;
    private String photoUrl;
    @Pattern(regexp = "WAITER_STAFF|COOKING_STAFF|STOCK_MANAGEMENT_STAFF", message = "Please select the valid staffType.")
    private String staffType;
}
