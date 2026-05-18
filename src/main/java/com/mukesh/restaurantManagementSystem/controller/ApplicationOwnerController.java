package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.ApplicationOwnerLoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.ApplicationOwnerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.UserInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerLoginResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.UserInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.ApplicationOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/application-owner")
@RequiredArgsConstructor
public class ApplicationOwnerController {
    private final ApplicationOwnerService applicationOwnerService;

    @PostMapping("/invite")
    public ResponseEntity<UserInviteResponseDTO> inviteRestaurantOwner(@RequestBody @Valid UserInviteRequestDTO request) {
        return ResponseEntity.ok(applicationOwnerService.inviteUser(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationOwnerRegisterResponseDTO> registerApplicationOwner(@RequestBody @Valid ApplicationOwnerRegisterRequestDTO request) {
        return ResponseEntity.ok(applicationOwnerService.registerApplicationOwner(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApplicationOwnerLoginResponseDTO> loginApplicationOwner(@RequestBody @Valid ApplicationOwnerLoginRequestDTO request) {
        return ResponseEntity.ok(applicationOwnerService.loginApplicationOwner(request));
    }
}
