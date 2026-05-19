package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/refresh-token")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/{refreshToken}")
    public ResponseEntity<LoginResponseDTO> getAccessToken(@PathVariable(name = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(refreshTokenService.reGenerateAccessToken(refreshToken));
    }
}

