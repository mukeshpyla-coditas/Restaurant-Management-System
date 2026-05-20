package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchRestaurantDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.ApplicationOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/application-owner")
@RequiredArgsConstructor
public class ApplicationOwnerController {
    private final ApplicationOwnerService applicationOwnerService;

    @PostMapping("/invite")
    public ResponseEntity<RestaurantOwnerInviteResponseDTO> inviteRestaurantOwner(@RequestBody @Valid RestaurantOwnerInviteRequestDTO request) {
        return ResponseEntity.ok(applicationOwnerService.inviteUser(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationOwnerRegisterResponseDTO> registerApplicationOwner(@RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.ok(applicationOwnerService.registerApplicationOwner(request));
    }

    @GetMapping("/fetch/{restaurantId}")
    public ResponseEntity<FetchRestaurantDetailsResponseDTO> fetchRestaurantDetails(
            @PathVariable(name = "restaurantId") Long restaurantId,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok(applicationOwnerService.fetchRestaurantDetails(restaurantId, size, page));
    }
}
