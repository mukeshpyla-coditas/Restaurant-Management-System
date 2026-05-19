package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.LoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.RefreshToken;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.repository.RefreshTokenRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.AuthService;
import com.mukesh.restaurantManagementSystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponseDTO loginUser(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String accessToken = jwtUtil.generateToken(request.getUsername(), Role.APPLICATION_OWNER.toString());
            String refreshToken = UUID.randomUUID().toString();
            RefreshToken refreshToken1 = RefreshToken.builder()
                    .refreshToken(refreshToken)
                    .user(usersRepository.findByUsername(request.getUsername()).orElseThrow(() -> new EntityNotFoundException("Specified user is not found.")))
                    .createdAt(LocalDate.now())
                    .expirationAt(LocalDate.now().plusDays(1))
                    .build();
            refreshTokenRepository.save(refreshToken1);

            return LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .message("The token will be active for next 10min.")
                    .build();
        } catch (Exception exception) {
            log.error("Exception occurred: {}", exception.getMessage());
        }

        return null;
    }
}
