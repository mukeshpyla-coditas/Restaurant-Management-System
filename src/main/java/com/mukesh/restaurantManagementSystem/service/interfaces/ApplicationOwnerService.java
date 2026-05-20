package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchRestaurantDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerInviteResponseDTO;

public interface ApplicationOwnerService {
    RestaurantOwnerInviteResponseDTO inviteUser(RestaurantOwnerInviteRequestDTO request);
    ApplicationOwnerRegisterResponseDTO registerApplicationOwner(RegisterRequestDTO request);
    FetchRestaurantDetailsResponseDTO fetchRestaurantDetails(Long restaurantId, Integer size, Integer page);
}
