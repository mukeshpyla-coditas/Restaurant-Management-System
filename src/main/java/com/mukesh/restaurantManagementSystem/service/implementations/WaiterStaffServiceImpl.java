package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.AddItemsToExistingOrderRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.CancelOrderItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.GenerateBillRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.OrderItemsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.AddItemsToExistingOrderResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.CancelOrderItemResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.GenerateBillResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.OrderItemsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewMenuResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Customers;
import com.mukesh.restaurantManagementSystem.entity.FoodItems;
import com.mukesh.restaurantManagementSystem.entity.Menu;
import com.mukesh.restaurantManagementSystem.entity.OrderItems;
import com.mukesh.restaurantManagementSystem.entity.Orders;
import com.mukesh.restaurantManagementSystem.entity.RestaurantTables;
import com.mukesh.restaurantManagementSystem.entity.Staff;
import com.mukesh.restaurantManagementSystem.entity.TableAssignments;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.OrderStatus;
import com.mukesh.restaurantManagementSystem.enums.PaymentStatus;
import com.mukesh.restaurantManagementSystem.enums.RestaurantType;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.exceptions.InvalidRequestException;
import com.mukesh.restaurantManagementSystem.exceptions.InvalidTypeException;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
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
    private Long counter = (long) 0;

    @Override
    public List<Integer> viewAssignedTables() {
        Staff existingStaff = getWaiterStaff();

        List<TableAssignments> assignedTables = existingStaff.getAssignedTables();
        if(assignedTables == null) {
            throw new NotAssignedException("Specified staff is not assigned to any tables yet.");
        }

        List<Integer> tables = new ArrayList<>();

        for(TableAssignments assignment : assignedTables) {
            tables.add(assignment.getAssignedTables().getTableNumber());
        }

        log.info("Fetched the assigned tables for the waiter: {}", existingStaff.getUser().getFullName());

        return tables;
    }

    @Override
    public Map<String, List<ViewMenuResponseDTO>> viewMenu() {
        Staff existingStaff = getWaiterStaff();
        System.out.println("Existing Staff: " + existingStaff);
        Menu branchSpecificMenu = existingStaff.getBranch().getMenu();
        System.out.println("Menu: " + branchSpecificMenu);
        List<FoodItems> foodItemsList = foodItemsRepository.findByMenu(branchSpecificMenu);
        System.out.println("FoodItems List: " + foodItemsList);

        Map<String, List<ViewMenuResponseDTO>> menu = new HashMap<>();

        for(FoodItems item : foodItemsList) {
            String itemCategory = item.getCategory().getCategoryName();
            log.info(itemCategory);
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

        log.info("Fetched the menu of the branch: {}", branchSpecificMenu.getBranch().getId());

        return menu;
    }

    @Override
    public OrderItemsResponseDTO orderItems(OrderItemsRequestDTO request) {
        Staff existingStaff = getWaiterStaff();
        RestaurantTables orderTable = tablesRepository.findByTableNumber(request.getTableNumber())
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
        newOrder.setPaymentStatus(PaymentStatus.PENDING);
        ordersRepository.save(newOrder);

        orderItemsList.forEach(orderItems -> orderItems.setOrder(newOrder));

        Customers customers = Customers.builder()
                .customerName(request.getCustomerName())
                .contactNumber(request.getContactNumber())
                .ordersList(List.of(newOrder))
                .build();
        customersRepository.save(customers);

        newOrder.setCustomer(customers);
        ordersRepository.save(newOrder);

        log.info("Added a order-items to a new Order. Order taken by {}", existingStaff.getUser().getFullName());

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
        log.info("An order-item is added to the existing order.");

        return AddItemsToExistingOrderResponseDTO.builder()
                .orderId(existingOrder.getId())
                .foodItemId(foodItem.getId())
                .foodItemName(foodItem.getItemName())
                .build();
    }

    @Override
    public CancelOrderItemResponseDTO cancelOrderItems(CancelOrderItemsRequestDTO request) {
        Orders activeOrder = ordersRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("There is not order found with specified orderId."));
        if(activeOrder.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
            throw new SessionExpirationException("Specified order is already completed(payment is done). Please re-confirm the orderId.");
        }
        OrderItems requestedOrderItem = orderItemsRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Specified orderItemId does not exist. Please re-confirm the orderItemId."));

        boolean flag = false;
        Map<String, Integer> remainingOrderItems = new HashMap<>();
        for(OrderItems activeOrderItems : activeOrder.getOrderItemsList()) {
            remainingOrderItems.put(activeOrderItems.getItem().getItemName(), activeOrderItems.getQuantity());
            if(activeOrderItems.equals(requestedOrderItem)) {
                flag = true;
            }
        }

        if(!flag) throw new InvalidTypeException("Specified orderItem is not part of specified order. Please re-verify the order and orderItem IDs.");

        requestedOrderItem.setOrderStatus(OrderStatus.CANCELLED);
        orderItemsRepository.save(requestedOrderItem);
        log.info("Cancelled order: {}", requestedOrderItem.getId());

        return CancelOrderItemResponseDTO.builder()
                .remainingOrders(remainingOrderItems)
                .message("Specified orderItem with ID: " + request.getItemId() + " is successfully cancelled. Thank you!")
                .build();
    }

    @Override
    public GenerateBillResponseDTO generateBill(GenerateBillRequestDTO request) {
        Staff waiterStaff = getWaiterStaff();
        Customers existingCustomer = customersRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Specified customer does not exist."));
        RestaurantTables existingRestaurantTable = tablesRepository.findByTableNumber(request.getTableNumber())
                .orElseThrow(() -> new EntityNotFoundException("Specified table does not exist."));

        Orders activeOrder = ordersRepository.findByCustomerAndRestaurantTable(existingCustomer, existingRestaurantTable)
               .orElseThrow(() -> new EntityNotFoundException("There are no active orders present for specified customer and restaurant table."));

        if(activeOrder.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
            throw new InvalidRequestException("Payment for the order placed by the specified customer is already done. Please re-confirm the customerId and tableId.");
        }

        Double totalPrice = 0.0;
        List<OrderItems> orderItemsList = activeOrder.getOrderItemsList();
        for(OrderItems orderItem : orderItemsList) {
            totalPrice += (orderItem.getItem().getPrice()) * orderItem.getQuantity();
        }
        Double taxPercentage = 0.0;

        if(waiterStaff.getBranch().getRestaurant().getRestaurantType().equals(RestaurantType.GENERAL)) taxPercentage = 5.0;
        else if(waiterStaff.getBranch().getRestaurant().getRestaurantType().equals(RestaurantType.LUXURY)) taxPercentage = 18.0;

        activeOrder.setPaymentStatus(PaymentStatus.COMPLETED);
        activeOrder.setDiscount(request.getDiscount());
        ordersRepository.save(activeOrder);

        Double totalAmount = totalPrice + (totalPrice * (taxPercentage / 100.0)) - activeOrder.getDiscount();
        log.info("Calculated the the totalAmount and taxPercentage for the order. Now, changing the status of the order to 'PAYMENT_COMPLETED'");

        PDDocument pdDocument = new PDDocument();
        PDPage pdPage = new PDPage(PDRectangle.A4);
        pdDocument.addPage(pdPage);

        PDPageContentStream contentStream = null;

        try {
            contentStream = new PDPageContentStream(pdDocument, pdPage);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
            contentStream.setStrokingColor(Color.RED);
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);

            contentStream.setLeading(14.5f);

            contentStream.newLineAtOffset(50, 750);

            contentStream.showText("Date: " + activeOrder.getPlacedAt());
            contentStream.newLine();

            contentStream.showText("Restaurant Name: " + waiterStaff.getBranch().getRestaurant().getRestaurantName());
            contentStream.newLine();

            contentStream.showText("Customer Name: " + existingCustomer.getCustomerName());
            contentStream.newLine();

            contentStream.showText("Customer Number: " + existingCustomer.getContactNumber());
            contentStream.newLine();

            contentStream.showText("Table Number: " + existingRestaurantTable.getTableNumber());
            contentStream.newLine();

            for(OrderItems orderItem : orderItemsList) {
                contentStream.showText("Item Name: " + orderItem.getItem().getItemName() + "    Quantity: " + orderItem.getQuantity() + "   Price: " + orderItem.getItem().getPrice());
                contentStream.newLine();
            }

            contentStream.showText("Tax Percentage: " + taxPercentage);
            contentStream.newLine();

            contentStream.showText("Discount Amount: " + activeOrder.getDiscount());
            contentStream.newLine();

            contentStream.showText("Total Amount: " + totalAmount);
            contentStream.newLine();

            contentStream.endText();
            contentStream.close();

            pdDocument.save("C:/Users/Coditas-Admin/Desktop/Projects/Restaurant Management Project/restaurantManagementSystem/Bills/Bill-" + ++counter + ".pdf");
            pdDocument.close();

        } catch (IOException e) {
            log.error("There is some exception occurred while working with PDDocument. Please verify.");
        }

        return GenerateBillResponseDTO.builder()
                .ordersAmount(totalPrice)
                .taxPercentage(taxPercentage)
                .totalAmount(totalAmount)
                .generatedBy(waiterStaff.getUser().getFullName())
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
