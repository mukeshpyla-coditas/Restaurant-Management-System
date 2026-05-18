package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerRegisterResponseDTO;

public interface RestaurantOwnerService {
    RestaurantOwnerRegisterResponseDTO registerOwner(RestaurantOwnerRegisterRequestDTO request);
}
