package com.mukesh.restaurantManagementSystem.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AddFoodItemsRequestDTO {
    private Long categoryId;
    private String itemName;
    private String itemDescription;
    private String imageUrl;
    private LocalDate createdAt;
    private String ingredients;
    private String preparationNotes;
    private Double calories;
    private Double price;
}
