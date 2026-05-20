package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchRestaurantDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.ApplicationOwnerService;
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

@RestController
@RequestMapping("/v1/application-owner")
@RequiredArgsConstructor
@Tag(name = "Application-Owner APIs")
public class ApplicationOwnerController {
    private final ApplicationOwnerService applicationOwnerService;

    @Operation(
            summary = "Sends invite to restaurant owners",
            description = "Application Owner can only access this endpoint and send invite to the restaurant owners, for onboarding."
    )
    @PostMapping("/invite")
    public ResponseEntity<RestaurantOwnerInviteResponseDTO> inviteRestaurantOwner(@RequestBody @Valid RestaurantOwnerInviteRequestDTO request) {
        return ResponseEntity.ok(applicationOwnerService.inviteUser(request));
    }

    @Operation(
            summary = "Registers Application Owner",
            description = "User(Application Owner) must enter all the fundamental data asked and submit the form. " +
                    "Then this API is called and saves the data of the Application Owner. " +
                    "NOTE that Application Owner must remember the username and password he/she entered."
    )
    @PostMapping("/register")
    public ResponseEntity<ApplicationOwnerRegisterResponseDTO> registerApplicationOwner(@RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.ok(applicationOwnerService.registerApplicationOwner(request));
    }

    @Operation(
            summary = "Fetches the details of the Restaurant Specified",
            description = "Application Owner has authority to access the details of restaurants registered under his application. " +
                    "NOTE that restaurantId must be passed onto the backend to fetch the details."
    )
    @GetMapping("/fetch/{restaurantId}")
    public ResponseEntity<FetchRestaurantDetailsResponseDTO> fetchRestaurantDetails(
            @PathVariable(name = "restaurantId") Long restaurantId,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok(applicationOwnerService.fetchRestaurantDetails(restaurantId, size, page));
    }
}
