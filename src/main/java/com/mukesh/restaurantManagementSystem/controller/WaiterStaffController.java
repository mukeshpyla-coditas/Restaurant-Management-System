package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.AddItemsToExistingOrderRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.CancelOrderItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.GenerateBillRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.OrderItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddItemsToExistingOrderResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.CancelOrderItemResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.GenerateBillResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.OrderItemsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewMenuResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.WaiterStaffService;
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
@RequestMapping("/v1/waiter-staff")
@RequiredArgsConstructor
@Tag(name = "Waiter-Staff APIs")
public class WaiterStaffController {
    private final WaiterStaffService waiterStaffService;

    @Operation(
            summary = "Waiter can view the assigned tables",
            description = "By just logging-in to the application, there is a tab visible to the waiter to view assigned-tables."
    )
    @GetMapping("/view-assigned-tables")
    public ResponseEntity<List<Integer>> viewAssignedTables() {
        return ResponseEntity.ok(waiterStaffService.viewAssignedTables());
    }

    @Operation(
            summary = "Waiter-Staff can view Menu of the restaurant(specific to the branch)",
            description = "Waiter staff will be able to select the items to order by viewing at the menu of the branch."
    )
    @GetMapping("/view-menu")
    public ResponseEntity<Map<String, List<ViewMenuResponseDTO>>> viewMenu() {
        return ResponseEntity.ok(waiterStaffService.viewMenu());
    }

    @Operation(
            summary = "Waiter-Staff takes order from the customer",
            description = "Waiter-staff will be able to take the order-details from the customer and mark the order as 'ORDER_PLACED'. " +
                    "These will be considered as active orders."
    )
    @PostMapping("/order-items")
    public ResponseEntity<OrderItemsResponseDTO> orderItems(@RequestBody @Valid OrderItemsRequestDTO request) {
        return ResponseEntity.ok(waiterStaffService.orderItems(request));
    }

    @Operation(
            summary = "Waiter-Staff can add order-items into an existing active order.",
            description = "If customer wants to add order-items into an existing-order, waiter can do that from this API."
    )
    @PostMapping("/add-items")
    public ResponseEntity<AddItemsToExistingOrderResponseDTO> addItemsToExistingOrder(@RequestBody @Valid AddItemsToExistingOrderRequestDTO request) {
        return ResponseEntity.ok(waiterStaffService.addItemsToExistingOrder(request));
    }

    @Operation(
            summary = "Waiter-Staff can cancel the order-item from an active-order",
            description = "Waiter-staff can cancel the order-item from the order, if the customer wants it to."
    )
    @PostMapping("/cancel-item")
    public ResponseEntity<CancelOrderItemResponseDTO> cancelOrderItem(@RequestBody @Valid CancelOrderItemsRequestDTO request) {
        return ResponseEntity.ok(waiterStaffService.cancelOrderItems(request));
    }

    @Operation(
            summary = "Waiter-Staff can generate bill once the customer is done with dining",
            description = "Waiter-Staff can click on 'Generate Bill' option. This generates the bill for the order placed by the customer and creates a PDF."
    )
    @PostMapping("/generate-bill")
    public ResponseEntity<GenerateBillResponseDTO> generateBill(@RequestBody @Valid GenerateBillRequestDTO request) {
        return ResponseEntity.ok(waiterStaffService.generateBill(request));
    }
}
