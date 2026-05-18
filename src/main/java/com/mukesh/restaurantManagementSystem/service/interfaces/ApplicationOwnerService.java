package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.ApplicationOwnerLoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.ApplicationOwnerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.UserInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerLoginResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.UserInviteResponseDTO;

public interface ApplicationOwnerService {
    UserInviteResponseDTO inviteUser(UserInviteRequestDTO request);
    ApplicationOwnerRegisterResponseDTO registerApplicationOwner(ApplicationOwnerRegisterRequestDTO request);
    ApplicationOwnerLoginResponseDTO loginApplicationOwner(ApplicationOwnerLoginRequestDTO request);
}
