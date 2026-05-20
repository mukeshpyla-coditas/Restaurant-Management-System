package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.LoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchRestaurantDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerInviteResponseDTO;

public interface ApplicationOwnerService {
    RestaurantOwnerInviteResponseDTO inviteUser(RestaurantOwnerInviteRequestDTO request);
    ApplicationOwnerRegisterResponseDTO registerApplicationOwner(RegisterRequestDTO request);
    LoginResponseDTO loginApplicationOwner(LoginRequestDTO request);
    FetchRestaurantDetailsResponseDTO fetchRestaurantDetails(Long restaurantId, Integer size, Integer page);
}
