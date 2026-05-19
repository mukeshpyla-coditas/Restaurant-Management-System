package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.RefreshToken;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.exceptions.SessionExpirationException;
import com.mukesh.restaurantManagementSystem.repository.RefreshTokenRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.RefreshTokenService;
import com.mukesh.restaurantManagementSystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponseDTO reGenerateAccessToken(String refreshToken) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new EntityNotFoundException("Provided refreshToken is not valid. Please re-verify the refreshToken."));

        if(existingRefreshToken.getExpirationAt().isBefore(LocalDate.now())) {
            refreshTokenRepository.delete(existingRefreshToken);
            throw new SessionExpirationException("Specified refreshToken is expired. Please re-login to generate a new access and refreshTokens.");
        }
        Users existingUser = existingRefreshToken.getUser();

        String accessToken = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getRole().toString());

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message("Re-generated the accessToken. NOTE that accessToken has validity of 10min.")
                .build();
    }
}
