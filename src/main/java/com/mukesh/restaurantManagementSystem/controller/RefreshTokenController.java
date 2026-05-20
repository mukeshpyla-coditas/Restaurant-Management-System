package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Refresh-Token APIs")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @Operation(
            summary = "Re-generates the accessToken, if the refreshToken is not yet expired",
            description = "Accepts the refreshToken and re-generates accessToken, only if refreshToken is not expired."
    )
    @GetMapping("/{refreshToken}")
    public ResponseEntity<LoginResponseDTO> getAccessToken(@PathVariable(name = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(refreshTokenService.reGenerateAccessToken(refreshToken));
    }
}

