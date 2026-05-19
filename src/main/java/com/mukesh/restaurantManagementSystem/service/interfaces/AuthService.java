package com.mukesh.restaurantManagementSystem.service.interfaces;


import com.mukesh.restaurantManagementSystem.dto.request.LoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO loginUser(LoginRequestDTO request);
}
