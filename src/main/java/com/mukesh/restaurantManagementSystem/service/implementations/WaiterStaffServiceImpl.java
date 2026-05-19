package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.AddItemsToExistingOrderRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.OrderItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddItemsToExistingOrderResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.OrderItemsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewMenuResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Customers;
import com.mukesh.restaurantManagementSystem.entity.FoodItems;
import com.mukesh.restaurantManagementSystem.entity.Menu;
import com.mukesh.restaurantManagementSystem.entity.OrderItems;
import com.mukesh.restaurantManagementSystem.entity.Orders;
import com.mukesh.restaurantManagementSystem.entity.RestaurantTables;
import com.mukesh.restaurantManagementSystem.entity.Staff;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.OrderStatus;
import com.mukesh.restaurantManagementSystem.enums.PaymentStatus;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.exceptions.NotAssignedException;
import com.mukesh.restaurantManagementSystem.exceptions.SessionExpirationException;
import com.mukesh.restaurantManagementSystem.repository.CustomersRepository;
import com.mukesh.restaurantManagementSystem.repository.FoodItemsRepository;
import com.mukesh.restaurantManagementSystem.repository.OrderItemsRepository;
import com.mukesh.restaurantManagementSystem.repository.OrdersRepository;
import com.mukesh.restaurantManagementSystem.repository.StaffRepository;
import com.mukesh.restaurantManagementSystem.repository.TablesRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.WaiterStaffService;
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
public class WaiterStaffServiceImpl implements WaiterStaffService {
    private final StaffRepository staffRepository;
    private final UsersRepository usersRepository;
    private final TablesRepository tablesRepository;
    private final OrdersRepository ordersRepository;
    private final CustomersRepository customersRepository;
    private final FoodItemsRepository foodItemsRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Override
    public List<Integer> viewAssignedTables() {
        Staff existingStaff = getWaiterStaff();

        List<RestaurantTables> restaurantTablesList = existingStaff.getAssignedTables();
        if(restaurantTablesList == null) {
            throw new NotAssignedException("Specified staff is not assigned to any tables yet.");
        }

        List<Integer> assignedTables = new ArrayList<>();

        for(RestaurantTables restaurantTable : restaurantTablesList) {
            assignedTables.add(restaurantTable.getTableNumber());
        }

        return assignedTables;
    }

    @Override
    public Map<String, List<ViewMenuResponseDTO>> viewMenu() {
        Staff existingStaff = getWaiterStaff();
        Menu branchSpecificMenu = existingStaff.getBranch().getMenu();
        List<FoodItems> foodItemsList = branchSpecificMenu.getItemsList();

        Map<String, List<ViewMenuResponseDTO>> menu = new HashMap<>();

        for(FoodItems item : foodItemsList) {
            String itemCategory = item.getCategory().getCategoryName();
            ViewMenuResponseDTO response = ViewMenuResponseDTO.builder()
                    .itemName(item.getItemName())
                    .itemDescription(item.getItemDescription())
                    .price(item.getPrice())
                    .calories(item.getCalories())
                    .ingredients(item.getIngredients())
                    .build();

            List<ViewMenuResponseDTO> list;
            if(menu.containsKey(itemCategory)) {
                list = menu.get(itemCategory);
            }
            else {
                list = new ArrayList<>();
            }
            list.add(response);
            menu.put(itemCategory, list);
        }

        return menu;
    }

    @Override
    public OrderItemsResponseDTO orderItems(OrderItemsRequestDTO request) {
        Staff existingStaff = getWaiterStaff();
        RestaurantTables orderTable = tablesRepository.findById(request.getTableNumber())
                .orElseThrow(() -> new EntityNotFoundException("Specified table does not exist."));
        Orders newOrder = Orders.builder()
                .restaurantTable(orderTable)
                .waiterStaff(existingStaff)
                .build();

        Map<Long, Integer> foodItems = request.getFoodItems();
        Map<String, Integer> orderedFoodItems = new HashMap<>();
        List<OrderItems> orderItemsList = new ArrayList<>();

        for(Map.Entry<Long, Integer> entry : foodItems.entrySet()) {
            FoodItems item = foodItemsRepository.findById(entry.getKey())
                    .orElseThrow(() -> new EntityNotFoundException("Specified item does not exist. Please re-confirm the itemId."));
            orderedFoodItems.put(item.getItemName(), entry.getValue());

            OrderItems orderItems = OrderItems.builder()
                    .item(item)
                    .quantity(entry.getValue())
                    .build();
            orderItemsRepository.save(orderItems);
            orderItemsList.add(orderItems);
        }

        newOrder.setOrderItemsList(orderItemsList);
        newOrder.setOrderStatus(OrderStatus.ORDER_PLACED);
        ordersRepository.save(newOrder);

        orderItemsList.forEach(orderItems -> orderItems.setOrder(newOrder));

        Customers customers = Customers.builder()
                .customerName(request.getCustomerName())
                .contactNumber(request.getContactNumber())
                .ordersList(List.of(newOrder))
                .build();
        customersRepository.save(customers);

        return OrderItemsResponseDTO.builder()
                .orderedItems(orderedFoodItems)
                .tableNumber(orderTable.getTableNumber())
                .customerName(customers.getCustomerName())
                .build();
    }

    @Override
    public AddItemsToExistingOrderResponseDTO addItemsToExistingOrder(AddItemsToExistingOrderRequestDTO request) {
        Orders existingOrder = ordersRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Specified orderId is not found. Please re-confirm the orderId."));
        if(existingOrder.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
           throw new SessionExpirationException("Specified order is already served. Please make a new order.");
        }

        FoodItems foodItem = foodItemsRepository.findById(request.getFoodItemId())
                .orElseThrow(() -> new EntityNotFoundException("Specified foodItemId does not exist. Please re-confirm the itemId."));

        OrderItems newOrderItem = OrderItems.builder()
                .item(foodItem)
                .order(existingOrder)
                .quantity(request.getQuantity())
                .build();
        orderItemsRepository.save(newOrderItem);

        existingOrder.getOrderItemsList().add(newOrderItem);

        return AddItemsToExistingOrderResponseDTO.builder()
                .orderId(existingOrder.getId())
                .foodItemId(foodItem.getId())
                .foodItemName(foodItem.getItemName())
                .build();
    }

    public Staff getWaiterStaff() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users existingUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Specified user does not exist."));
        return staffRepository.findByUser(existingUser)
                .orElseThrow(() -> new EntityNotFoundException("Staff with specified user details does not exist."));
    }
}
