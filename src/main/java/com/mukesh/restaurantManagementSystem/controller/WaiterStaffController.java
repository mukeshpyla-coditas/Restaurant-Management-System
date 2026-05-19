package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.AddItemsToExistingOrderRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.OrderItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddItemsToExistingOrderResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.OrderItemsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewMenuResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.WaiterStaffService;
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
@RequestMapping("/v1/waiter-staff")
@RequiredArgsConstructor
public class WaiterStaffController {
    private final WaiterStaffService waiterStaffService;

    @GetMapping("/view-assigned-tables")
    public ResponseEntity<List<Integer>> viewAssignedTables() {
        return ResponseEntity.ok(waiterStaffService.viewAssignedTables());
    }

    @GetMapping("/view-menu")
    public ResponseEntity<Map<String, List<ViewMenuResponseDTO>>> viewMenu() {
        return ResponseEntity.ok(waiterStaffService.viewMenu());
    }

    @PostMapping("/order-items")
    public ResponseEntity<OrderItemsResponseDTO> orderItems(@RequestBody @Valid OrderItemsRequestDTO request) {
        return ResponseEntity.ok(waiterStaffService.orderItems(request));
    }

    @PostMapping("/add-items")
    public ResponseEntity<AddItemsToExistingOrderResponseDTO> addItemsToExistingOrder(@RequestBody @Valid AddItemsToExistingOrderRequestDTO request) {
        return ResponseEntity.ok(waiterStaffService.addItemsToExistingOrder(request));
    }
}
