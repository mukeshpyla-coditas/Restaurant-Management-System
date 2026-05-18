package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Setter
@Getter
public class ManagerRegisterRequestDTO {
    @NotNull
    private String fullName;
    @NotNull
    private String username;
    @Email
    private String email;
    @Size(min = 6, max = 8)
    private String password;
    @Pattern(regexp = "MALE|FEMALE|OTHERS")
    private String gender;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Size(min = 12, max = 12, message = "Aadhar Number must be of 12 characters.")
    private String aadharNumber;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Size(min = 10, max = 10, message = "Contact Number must be of 10 characters.")
    private String contactNumber;
    @NotNull
    private String photoUrl;
    @NotNull
    private Long branchId;
}
