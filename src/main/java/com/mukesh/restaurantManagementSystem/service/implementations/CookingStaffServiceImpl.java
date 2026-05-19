package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.response.ViewActiveOrdersResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Branches;
import com.mukesh.restaurantManagementSystem.entity.FoodItems;
import com.mukesh.restaurantManagementSystem.entity.OrderItems;
import com.mukesh.restaurantManagementSystem.entity.Orders;
import com.mukesh.restaurantManagementSystem.entity.RestaurantTables;
import com.mukesh.restaurantManagementSystem.entity.Staff;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.OrderStatus;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.repository.FoodItemsRepository;
import com.mukesh.restaurantManagementSystem.repository.StaffRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.CookingStaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CookingStaffServiceImpl implements CookingStaffService {
    private final UsersRepository usersRepository;
    private final StaffRepository staffRepository;
    private final FoodItemsRepository foodItemsRepository;

    @Override
    public Map<Long, List<ViewActiveOrdersResponseDTO>> viewActiveOrders() {
        Staff cookingStaff = getCookingStaff();
        Branches branch = cookingStaff.getBranch();
        List<RestaurantTables> restaurantTables = branch.getRestaurantTablesList();

        Map<Long, List<ViewActiveOrdersResponseDTO>> activeOrders = new HashMap<>();
        for(RestaurantTables restaurantTable : restaurantTables) {
            List<Orders> ordersList = restaurantTable.getOrdersList();
            for(Orders order : ordersList) {
                List<ViewActiveOrdersResponseDTO> activeOrdersResponse = new ArrayList<>();
                for (OrderItems items : order.getOrderItemsList()) {
                    if (items.getOrderStatus().equals(OrderStatus.ORDER_PLACED)) {
                        FoodItems foodItem = items.getItem();
                        ViewActiveOrdersResponseDTO response = ViewActiveOrdersResponseDTO.builder()
                                .itemName(foodItem.getItemName())
                                .preparationNotes(foodItem.getPreparationNotes())
                                .ingredients(foodItem.getIngredients())
                                .orderStatus(items.getOrderStatus().toString())
                                .build();
                        activeOrdersResponse.add(response);
                    }
                }
                activeOrders.put(order.getId(), activeOrdersResponse);
            }
        }

        return activeOrders;
    }

    public Staff getCookingStaff() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users existingUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Specified user does not exist."));

        return staffRepository.findByUser(existingUser)
                .orElseThrow(() -> new EntityNotFoundException("Specified staff does not exist."));
    }
}
