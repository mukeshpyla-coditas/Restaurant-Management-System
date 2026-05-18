package com.mukesh.restaurantManagementSystem.dto.request;

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
    @Size(min = 2, max = 12, message = "The table seating capacity should be between 2 to 12")
    private Integer tableSeatingCapacity;

}
