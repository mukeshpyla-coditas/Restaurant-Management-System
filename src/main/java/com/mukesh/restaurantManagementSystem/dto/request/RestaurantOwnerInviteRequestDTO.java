package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOwnerInviteRequestDTO {
    @NonNull
    private Long senderId;

    @Email
    @NotNull
    private String receiverEmail;

    @NotBlank
    private String inviteMessage;
}
