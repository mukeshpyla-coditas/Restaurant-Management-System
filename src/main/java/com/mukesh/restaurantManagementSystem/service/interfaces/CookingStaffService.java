package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.response.ViewActiveOrdersResponseDTO;

import java.util.List;
import java.util.Map;

public interface CookingStaffService {
    Map<Long, List<ViewActiveOrdersResponseDTO>> viewActiveOrders();
}
