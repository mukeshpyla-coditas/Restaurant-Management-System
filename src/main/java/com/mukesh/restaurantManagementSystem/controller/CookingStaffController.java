package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.response.ViewActiveOrdersResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.CookingStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cooking-staff")
public class CookingStaffController {
    private final CookingStaffService cookingStaffService;

    @PostMapping("/view-active-orders")
    public ResponseEntity<Map<Long, List<ViewActiveOrdersResponseDTO>>> viewActiveOrders() {
        return ResponseEntity.ok(cookingStaffService.viewActiveOrders());
    }

}
