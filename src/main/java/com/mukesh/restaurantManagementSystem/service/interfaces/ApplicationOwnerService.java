package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.ApplicationOwnerLoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerLoginResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerInviteResponseDTO;

public interface ApplicationOwnerService {
    RestaurantOwnerInviteResponseDTO inviteUser(RestaurantOwnerInviteRequestDTO request);
    ApplicationOwnerRegisterResponseDTO registerApplicationOwner(RegisterRequestDTO request);
    ApplicationOwnerLoginResponseDTO loginApplicationOwner(ApplicationOwnerLoginRequestDTO request);
}
