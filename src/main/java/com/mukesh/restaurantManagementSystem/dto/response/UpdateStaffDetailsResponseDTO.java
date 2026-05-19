package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UpdateStaffDetailsResponseDTO {
    private String fullName;
    private String username;
    private String email;
    private String gender;
    private String aadharNumber;
    private String contactNumber;
    private String photoUrl;
    private String staffType;
}
