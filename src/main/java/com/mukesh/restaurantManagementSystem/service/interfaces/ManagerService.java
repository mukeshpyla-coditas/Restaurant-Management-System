package com.mukesh.restaurantManagementSystem.service.interfaces;

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

import java.util.List;

public interface ManagerService {
    ManagerRegisterResponseDTO registerManager(String inviteToken, ManagerRegisterRequestDTO request);
    AddStaffResponseDTO addStaff(AddStaffRequestDTO request);
    MenuCreationResponseDTO createMenu(Long branchId);
    AddCategoryResponseDTO addCategory(AddCategoryRequestDTO request);
    AddFoodItemsResponseDTO addFoodItems(AddFoodItemsRequestDTO request);
    AddTableResponseDTO addTable(AddTableRequestDTO request);
    AssignmentResponseDTO assignStaff(AssignmentRequestDTO request);
    AssignmentResponseDTO temporaryAssignment(AssignmentRequestDTO request);
    UpdateStaffDetailsResponseDTO updateStaffDetails(UpdateStaffDetailsRequestDTO request);
    String deleteStaffById(Long staffId);
    List<FetchStaffDetailsResponseDTO> fetchStaffDetails(Long branchId, Integer size, Integer page);
}
