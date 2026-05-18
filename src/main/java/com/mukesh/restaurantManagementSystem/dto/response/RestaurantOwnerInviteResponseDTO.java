package com.mukesh.restaurantManagementSystem.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RestaurantOwnerInviteResponseDTO {
    @NonNull
    @Email
    private String sentTo;

    @NotBlank
    private String message;
}
