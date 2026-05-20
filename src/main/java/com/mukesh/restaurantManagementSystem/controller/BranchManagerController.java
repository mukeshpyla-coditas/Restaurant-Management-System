package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.AddCategoryRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AddFoodItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AddStaffRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AddTableRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.AssignmentRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.ManagerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.UpdateStaffDetailsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddCategoryResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddFoodItemsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddStaffResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddTableResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AssignmentResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchStaffDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ManagerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.MenuCreationResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.UpdateStaffDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<AssignmentResponseDTO> assignStaff(@RequestBody @Valid AssignmentRequestDTO request) {
        return ResponseEntity.ok(managerService.assignStaff(request));
    }

    @PostMapping("/temporary-assignment")
    public ResponseEntity<AssignmentResponseDTO> temporaryAssignment(@RequestBody @Valid AssignmentRequestDTO request) {
        return ResponseEntity.accepted().body(managerService.temporaryAssignment(request));
    }

    @PostMapping("/update-staff-details")
    public ResponseEntity<UpdateStaffDetailsResponseDTO> updateStaffDetails(@RequestBody @Valid UpdateStaffDetailsRequestDTO request) {
        return ResponseEntity.ok(managerService.updateStaffDetails(request));
    }

    @DeleteMapping("/delete-staff/{staffId}")
    public String deleteStaff(@PathVariable(name = "staffId") Long staffId) {
        return managerService.deleteStaffById(staffId);
    }

    @GetMapping("/fetch-details/{branchId}")
    public ResponseEntity<List<FetchStaffDetailsResponseDTO>> fetchStaffDetails(
            @PathVariable(name = "branchId") Long branchId,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok(managerService.fetchStaffDetails(branchId, size, page));
    }


}
