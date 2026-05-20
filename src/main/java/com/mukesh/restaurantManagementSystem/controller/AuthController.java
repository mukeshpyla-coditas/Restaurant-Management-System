package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.LoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.ManagerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ManagerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.AuthService;
import com.mukesh.restaurantManagementSystem.service.interfaces.ManagerService;
import com.mukesh.restaurantManagementSystem.service.interfaces.RestaurantOwnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth APIs")
public class AuthController {
    private final AuthService authService;
    private final ManagerService managerService;
    private final RestaurantOwnerService restaurantOwnerService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }

    @PostMapping("/branch-manager/register/{inviteCode}")
    public ResponseEntity<ManagerRegisterResponseDTO> registerBranchManager(@PathVariable(name = "inviteCode") String inviteCode,
                                                                            @RequestBody @Valid ManagerRegisterRequestDTO request) {
        return ResponseEntity.ok(managerService.registerManager(inviteCode, request));
    }

    @PostMapping("/restaurant-owner/register/{inviteCode}")
    public ResponseEntity<RestaurantOwnerRegisterResponseDTO> registerRestaurantOwner(@PathVariable(name = "inviteCode") String inviteCode,
                                                                                      @RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.registerOwner(inviteCode, request));
    }
}
