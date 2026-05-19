package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddTableRequestDTO {
    @NotNull
    private Integer tableNumber;

    @NotNull
    @Max(value = 12)
    private Integer tableSeatingCapacity;

}
