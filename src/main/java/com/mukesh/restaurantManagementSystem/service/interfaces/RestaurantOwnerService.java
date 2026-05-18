package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.BranchManagerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.BranchRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.BranchManagerInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.BranchRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantRegisterResponseDTO;

public interface RestaurantOwnerService {
    RestaurantOwnerRegisterResponseDTO registerOwner(String inviteCode, RegisterRequestDTO request);
    RestaurantRegisterResponseDTO registerRestaurant(RestaurantRegisterRequestDTO request);
    BranchRegisterResponseDTO registerBranch(BranchRegisterRequestDTO request);
    BranchManagerInviteResponseDTO inviteBranchManager(BranchManagerInviteRequestDTO request);
}
