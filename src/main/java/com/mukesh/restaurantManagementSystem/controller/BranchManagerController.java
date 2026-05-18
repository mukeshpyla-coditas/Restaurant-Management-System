package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.AddCategoryRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AddFoodItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AddStaffRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AddTableRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AssignStaffRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.ManagerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddCategoryResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddFoodItemsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddStaffResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddTableResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AssignStaffResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ManagerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.MenuCreationResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/branch-manager")
@RequiredArgsConstructor
public class BranchManagerController {
    private final ManagerService managerService;

    @PostMapping("/register/{inviteCode}")
    public ResponseEntity<ManagerRegisterResponseDTO> registerBranchManager(@PathVariable(name = "inviteCode") String inviteCode,
                                                                            @RequestBody @Valid ManagerRegisterRequestDTO request) {
        return ResponseEntity.ok(managerService.registerManager(inviteCode, request));
    }

    @PostMapping("/add-staff")
    public ResponseEntity<AddStaffResponseDTO> addStaff(@RequestBody @Valid AddStaffRequestDTO request) {
        return ResponseEntity.ok(managerService.addStaff(request));
    }

    @PostMapping("/create-menu/{branchId}")
    public ResponseEntity<MenuCreationResponseDTO> createMenu(@PathVariable(name = "branchId") Long branchId) {
        return ResponseEntity.status(201).body(managerService.createMenu(branchId));
    }

    @PostMapping("/add-category")
    public ResponseEntity<AddCategoryResponseDTO> addCategory(@RequestBody @Valid AddCategoryRequestDTO request) {
        return ResponseEntity.status(201).body(managerService.addCategory(request));
    }

    @PostMapping("/add-food-items")
    public ResponseEntity<AddFoodItemsResponseDTO> addFoodItems(@RequestBody @Valid AddFoodItemsRequestDTO request) {
        return ResponseEntity.status(201).body(managerService.addFoodItems(request));
    }

    @PostMapping("/add-table")
    public ResponseEntity<AddTableResponseDTO> addTable(@RequestBody @Valid AddTableRequestDTO request) {
        return ResponseEntity.status(201).body(managerService.addTable(request));
    }

    @PostMapping("/assign-staff")
    public ResponseEntity<AssignStaffResponseDTO> assignStaff(@RequestBody @Valid AssignStaffRequestDTO request) {
        return ResponseEntity.ok(managerService.assignStaff(request));
    }

}
