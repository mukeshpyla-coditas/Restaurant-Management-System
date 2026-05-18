package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddCategoryRequestDTO {
    @NotNull
    private String categoryName;

    @NotNull
    private String categoryDescription;
}
