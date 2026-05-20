package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.BranchManagerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.BranchRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.BranchManagerInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.BranchRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchBranchDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.RestaurantOwnerService;
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

import java.util.List;

@RestController
@RequestMapping("/v1/restaurant-owner")
@RequiredArgsConstructor
public class RestaurantOwnerController {
    private final RestaurantOwnerService restaurantOwnerService;

    @PostMapping("/register/{inviteCode}")
    public ResponseEntity<RestaurantOwnerRegisterResponseDTO> registerRestaurantOwner(@PathVariable(name = "inviteCode") String inviteCode,
                                                                                      @RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.registerOwner(inviteCode, request));
    }

    @PostMapping("/register-restaurants")
    public ResponseEntity<RestaurantRegisterResponseDTO> registerRestaurants(@RequestBody @Valid RestaurantRegisterRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.registerRestaurant(request));
    }

    @PostMapping("/register-branches")
    public ResponseEntity<BranchRegisterResponseDTO> registerBranches(@RequestBody @Valid BranchRegisterRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.registerBranch(request));
    }

    @PostMapping("/invite")
    public ResponseEntity<BranchManagerInviteResponseDTO> registerBranchManager(@RequestBody @Valid BranchManagerInviteRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.inviteBranchManager(request));
    }

    @GetMapping("/fetch/{restaurantId}")
    public ResponseEntity<List<FetchBranchDetailsResponseDTO>> fetchBranchDetails(
            @PathVariable(name = "restaurantId") Long restaurantId,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok(restaurantOwnerService.fetchBranchDetails(restaurantId, size,page));
    }
}
