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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Branch-Manager APIs")
public class BranchManagerController {
    private final ManagerService managerService;

    @Operation(
            summary = "Manager adds the New-Staff and registers them.",
            description = "NOTE that it is the responsibility of Branch Manager to assign the username and password for the Registered staff. " +
                    "It is the responsibility of the registered staff member to remember his/her details."
    )
    @PostMapping("/add-staff")
    public ResponseEntity<AddStaffResponseDTO> addStaff(@RequestBody @Valid AddStaffRequestDTO request) {
        return ResponseEntity.ok(managerService.addStaff(request));
    }

    @Operation(
            summary = "Manager adds the menu for the respective branch",
            description = "Manager enters the required details of the branch and clicks the 'Create Menu' option which initializes a menu instance, into which food-items can be added."
    )
    @PostMapping("/create-menu/{branchId}")
    public ResponseEntity<MenuCreationResponseDTO> createMenu(@PathVariable(name = "branchId") Long branchId) {
        return ResponseEntity.status(201).body(managerService.createMenu(branchId));
    }

    @Operation(
            summary = "Manager adds Food Categories",
            description = "Manager will be able to add new categories into the menu. " +
                    "NOTE that, it is preferred to create the food-category before creating the food-items."
    )
    @PostMapping("/add-category")
    public ResponseEntity<AddCategoryResponseDTO> addCategory(@RequestBody @Valid AddCategoryRequestDTO request) {
        return ResponseEntity.status(201).body(managerService.addCategory(request));
    }

    @Operation(
            summary = "Manager adds the food-items, as per the Category",
            description = "Manager has the authority to add items into the menu specific to the branch."
    )
    @PostMapping("/add-food-items")
    public ResponseEntity<AddFoodItemsResponseDTO> addFoodItems(@RequestBody @Valid AddFoodItemsRequestDTO request) {
        return ResponseEntity.status(201).body(managerService.addFoodItems(request));
    }

    @Operation(
            summary = "Manager adds the restaurant-tables into the branch",
            description = "Manager can add the dining-tables into the branch instance, entering the required details of the table."
    )
    @PostMapping("/add-table")
    public ResponseEntity<AddTableResponseDTO> addTable(@RequestBody @Valid AddTableRequestDTO request) {
        return ResponseEntity.status(201).body(managerService.addTable(request));
    }

    @Operation(
            summary = "Manage assigns staff(waiter-staff) to the registered dining-tables",
            description = "NOTE that, manager can assign only the waiter-staff to the dining tables. " +
                    "He can select the waiter-id and list of dining tables, and assign the tables to the selected waiter-staff."
    )
    @PostMapping("/assign-staff")
    public ResponseEntity<AssignmentResponseDTO> assignStaff(@RequestBody @Valid AssignmentRequestDTO request) {
        return ResponseEntity.ok(managerService.assignStaff(request));
    }

    @Operation(
            summary = "Manager can make temporary-assignments",
            description = "If a particular waiter is in-active(not present), manager can temporarily assign another waiter to those tables." +
                    " There is like a toggle option(true/false) present, for temporary assignment."
    )
    @PostMapping("/temporary-assignment")
    public ResponseEntity<AssignmentResponseDTO> temporaryAssignment(@RequestBody @Valid AssignmentRequestDTO request) {
        return ResponseEntity.accepted().body(managerService.temporaryAssignment(request));
    }

    @Operation(
            summary = "Manager can update the details of the staff",
            description = "Manager can take the details of the staff and change the respective fields where changes are required."
    )
    @PostMapping("/update-staff-details")
    public ResponseEntity<UpdateStaffDetailsResponseDTO> updateStaffDetails(@RequestBody @Valid UpdateStaffDetailsRequestDTO request) {
        return ResponseEntity.ok(managerService.updateStaffDetails(request));
    }

    @Operation(
            summary = "Manager can delete a staff",
            description = "Manager can select the staff to delete. Once the manager clicks on 'delete staff' option, the staffId comes to the backend and respective staff is deleted."
    )
    @DeleteMapping("/delete-staff/{staffId}")
    public String deleteStaff(@PathVariable(name = "staffId") Long staffId) {
        return managerService.deleteStaffById(staffId);
    }

    @Operation(
            summary = "Manager fetches the details of all the staff present in his/her branch",
            description = "Manager can access the details of the staff-members present in the respective branch. " +
                    "NOTE that, the size and page number is also required to be passed onto the backend to apply pagination."
    )
    @GetMapping("/fetch-details/{branchId}")
    public ResponseEntity<List<FetchStaffDetailsResponseDTO>> fetchStaffDetails(
            @PathVariable(name = "branchId") Long branchId,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok(managerService.fetchStaffDetails(branchId, size, page));
    }


}
