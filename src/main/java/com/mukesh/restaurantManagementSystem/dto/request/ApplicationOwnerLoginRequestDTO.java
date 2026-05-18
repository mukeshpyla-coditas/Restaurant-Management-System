package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationOwnerLoginRequestDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
