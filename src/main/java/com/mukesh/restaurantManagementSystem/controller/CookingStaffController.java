package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.PrepareOrderRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.PrepareOrderResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewActiveOrdersResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.CookingStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cooking-staff")
@Tag(name = "Cooking-Staff APIs")
public class CookingStaffController {
    private final CookingStaffService cookingStaffService;

    @Operation(
            summary = "Cooking staff can view all active orders",
            description = "All the orders which are placed and are active will be visible to the cooking-staff."
    )
    @GetMapping("/view-active-orders")
    public ResponseEntity<Map<Long, List<ViewActiveOrdersResponseDTO>>> viewActiveOrders() {
        return ResponseEntity.ok(cookingStaffService.viewActiveOrders());
    }

    @Operation(
            summary = "Cooking-Staff can select an order from active orders to prepare",
            description = "From the list of active orders, cookingStaff can select a particular item and start preparing the order."
    )
    @PostMapping("/prepare-order")
    public ResponseEntity<PrepareOrderResponseDTO> prepareOrder(@RequestBody @Valid PrepareOrderRequestDTO request) {
        return ResponseEntity.ok(cookingStaffService.prepareOrder(request));
    }
}
