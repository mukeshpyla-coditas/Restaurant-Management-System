package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Setter
@Getter
public class RestaurantRegisterRequestDTO {
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Long ownerId;

    @NotNull
    private String restaurantName;

    @NotNull
    @Pattern(regexp = "GENERAL|LUXURY", message = "Please select a valid category: ['LUXURY', 'GENERAL']")
    private String restaurantType;

}
