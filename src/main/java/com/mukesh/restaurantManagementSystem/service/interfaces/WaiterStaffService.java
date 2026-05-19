package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.AddItemsToExistingOrderRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.OrderItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddItemsToExistingOrderResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.OrderItemsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewMenuResponseDTO;

import java.util.List;
import java.util.Map;

public interface WaiterStaffService {
    List<Integer> viewAssignedTables();
    Map<String, List<ViewMenuResponseDTO>> viewMenu();
    OrderItemsResponseDTO orderItems(OrderItemsRequestDTO request);
    AddItemsToExistingOrderResponseDTO addItemsToExistingOrder(AddItemsToExistingOrderRequestDTO request);
}
