package com.mukesh.restaurantManagementSystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ApplicationOwnerLoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String message;
}
