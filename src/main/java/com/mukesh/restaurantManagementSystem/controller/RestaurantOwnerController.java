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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Restaurant-Owner APIs")
public class RestaurantOwnerController {
    private final RestaurantOwnerService restaurantOwnerService;

    @Operation(
            summary = "Restaurant Owner registers New-Restaurants",
            description = "Restaurant Owner will enter all the required details of the restaurant. " +
                    "Once the form is submitted, the restaurant will be registered under the user's ownership."
    )
    @PostMapping("/register-restaurants")
    public ResponseEntity<RestaurantRegisterResponseDTO> registerRestaurants(@RequestBody @Valid RestaurantRegisterRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.registerRestaurant(request));
    }

    @Operation(
            summary = "Restaurant Owner registers branches of the Restaurant.",
            description = "Restaurant Owner will be able to register branches of restaurant only when the Restaurant is already registered by the User(Owner). Else there will be an exception raised."
    )
    @PostMapping("/register-branches")
    public ResponseEntity<BranchRegisterResponseDTO> registerBranches(@RequestBody @Valid BranchRegisterRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.registerBranch(request));
    }

    @Operation(
            summary = "Restaurant Owner sends invite to Branch Manager",
            description = "Restaurant Owner can send an invite to onboard the Branch Manager. " +
                    "The email of the Branch Manager should be entered by the restaurant owner."
    )
    @PostMapping("/invite")
    public ResponseEntity<BranchManagerInviteResponseDTO> registerBranchManager(@RequestBody @Valid BranchManagerInviteRequestDTO request) {
        return ResponseEntity.ok(restaurantOwnerService.inviteBranchManager(request));
    }

    @Operation(
            summary = "Fetches the details of all the branches of the specified restaurant",
            description = "NOTE that, this API can only be accessed by Restaurant-Owner. " +
                    "Restaurant-Owner who is accessing this API must be the actual owner of the requested restaurant."
    )
    @GetMapping("/fetch/{restaurantId}")
    public ResponseEntity<List<FetchBranchDetailsResponseDTO>> fetchBranchDetails(
            @PathVariable(name = "restaurantId") Long restaurantId,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok(restaurantOwnerService.fetchBranchDetails(restaurantId, size,page));
    }
}
