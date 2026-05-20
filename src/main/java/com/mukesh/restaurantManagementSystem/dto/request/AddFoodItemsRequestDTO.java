package com.mukesh.restaurantManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AddFoodItemsRequestDTO {
    @NotNull
    private Long categoryId;
    @NotNull
    private String itemName;
    @NotNull
    private String itemDescription;
    @NotNull
    private String imageUrl;
    @NotNull
    private LocalDate createdAt;
    @NotNull
    private String ingredients;
    @NotNull
    private String preparationNotes;
    @NotNull
    private Double calories;
    @NotNull
    private Double price;
}
