package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;

public interface RefreshTokenService {
    LoginResponseDTO reGenerateAccessToken(String refreshToken);
}
