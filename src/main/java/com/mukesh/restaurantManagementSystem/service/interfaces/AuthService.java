package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.UserInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.UserInviteResponseDTO;

public interface AuthService {
    UserInviteResponseDTO inviteUser(UserInviteRequestDTO request);
}
