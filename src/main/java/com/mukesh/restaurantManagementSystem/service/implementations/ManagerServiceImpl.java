package com.mukesh.restaurantManagementSystem.service.implementations;

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
import com.mukesh.restaurantManagementSystem.entity.Branches;
import com.mukesh.restaurantManagementSystem.entity.FoodItems;
import com.mukesh.restaurantManagementSystem.entity.Managers;
import com.mukesh.restaurantManagementSystem.entity.Menu;
import com.mukesh.restaurantManagementSystem.entity.MenuCategory;
import com.mukesh.restaurantManagementSystem.entity.RestaurantTables;
import com.mukesh.restaurantManagementSystem.entity.Staff;
import com.mukesh.restaurantManagementSystem.entity.TableAssignments;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.InviteStatus;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.enums.TableStatus;
import com.mukesh.restaurantManagementSystem.exceptions.BadRequestException;
import com.mukesh.restaurantManagementSystem.exceptions.CodeExpiredException;
import com.mukesh.restaurantManagementSystem.exceptions.CreationException;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.repository.BranchRepository;
import com.mukesh.restaurantManagementSystem.repository.FoodItemsRepository;
import com.mukesh.restaurantManagementSystem.repository.InvitationRepository;
import com.mukesh.restaurantManagementSystem.repository.ManagerRepository;
import com.mukesh.restaurantManagementSystem.repository.MenuCategoryRepository;
import com.mukesh.restaurantManagementSystem.repository.MenuRepository;
import com.mukesh.restaurantManagementSystem.repository.StaffRepository;
import com.mukesh.restaurantManagementSystem.repository.TableAssignmentsRepository;
import com.mukesh.restaurantManagementSystem.repository.TablesRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerServiceImpl implements ManagerService {
    private final CommonServiceImpl commonService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final StaffRepository staffRepository;
    private final MenuRepository menuRepository;
    private final BranchRepository branchRepository;
    private final TablesRepository tablesRepository;
    private final FoodItemsRepository foodItemsRepository;
    private final ManagerRepository managerRepository;
    private final InvitationRepository invitationRepository;
    private final MenuCategoryRepository categoryRepository;
    private final TableAssignmentsRepository tableAssignmentsRepository;

    @Override
    public ManagerRegisterResponseDTO registerManager(String inviteCode, ManagerRegisterRequestDTO request) {
        if(!commonService.isInviteTokenValid(inviteCode)) {
            throw new CodeExpiredException("InviteCode is expired. Please wait for the next invite mail.");
        }

        invitationRepository.findByInviteCode(inviteCode).setInviteStatus(InviteStatus.ACCEPTED);

        Users newUser = Users.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .aadharNumber(request.getAadharNumber())
                .contactNumber(request.getContactNumber())
                .isActive(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .joinedAt(LocalDate.now())
                .photoUrl(request.getPhotoUrl())
                .gender(commonService.checkGender(request.getGender()))
                .role(Role.BRANCH_MANAGER)
                .build();

        usersRepository.save(newUser);
        log.info("New user has been successfully registered!");

        Branches existingBranch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Specified Branch does not exist. Please re-verify the branchId."));

        Managers manager = Managers.builder()
                .branch(existingBranch)
                .user(newUser)
                .build();

        managerRepository.save(manager);
        log.info("Manager is successfully created. Manager Name: {}", manager.getUser().getFullName());

        existingBranch.setManager(manager);
        log.info("Assigning the manager to the existing branch: {}", existingBranch.getBranchName());

        return ManagerRegisterResponseDTO.builder()
                .managerName(newUser.getFullName())
                .managerId(manager.getId())
                .branchName(existingBranch.getBranchName())
                .restaurantName(existingBranch.getRestaurant().getRestaurantName())
                .restaurantOwnerName(existingBranch.getRestaurant().getOwner().getOwner().getFullName())
                .build();
    }

    @Override
    public AddStaffResponseDTO addStaff(AddStaffRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users newUser = Users.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .aadharNumber(request.getAadharNumber())
                .contactNumber(request.getContactNumber())
                .isActive(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .joinedAt(LocalDate.now())
                .photoUrl(request.getPhotoUrl())
                .gender(commonService.checkGender(request.getGender()))
                .role(commonService.checkStaffType(request.getStaffType()))
                .build();

        usersRepository.save(newUser);

        Users existingUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Specified user is not found."));
        Managers existingManager = managerRepository.findByUser(existingUser)
                .orElseThrow(() -> new EntityNotFoundException("Specified manager is not found."));

        Staff staff = Staff.builder()
                .branch(existingManager.getBranch())
                .isActive(true)
                .manager(existingManager)
                .user(newUser)
                .build();

        log.info("Created the new Staff instance. StaffId: {}", staff.getId());

        staffRepository.save(staff);
        existingManager.getStaffList().add(staff);

        return AddStaffResponseDTO.builder()
                .staffId(staff.getId())
                .staffName(staff.getUser().getFullName())
                .branchName(existingManager.getBranch().getBranchName())
                .managerName(existingManager.getUser().getFullName())
                .staffType(staff.getUser().getRole().toString())
                .build();
    }

    @Override
    public MenuCreationResponseDTO createMenu(Long branchId) {
        Branches existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Specified branch does not exist. PLease re-confirm the branchId."));
        if(existingBranch.getMenu() != null) {
            throw new CreationException("Menu is already created for the specified branch. Duplicate menus are not allowed.");
        }

        Menu menu = Menu.builder()
                .branch(existingBranch)
                .addedAt(LocalDate.now())
                .build();

        menuRepository.save(menu);
        log.info("Menu has been created for branch: {}", existingBranch.getBranchName());

        existingBranch.setMenu(menu);
        branchRepository.save(existingBranch);

        return MenuCreationResponseDTO.builder()
                .branchId(existingBranch.getId())
                .createdBy(existingBranch.getManager().getUser().getFullName())
                .message("Menu has been successfully created for the branch: " + existingBranch.getBranchName() + ". Can proceed to adding food items and category to the menu.")
                .build();
    }

    @Override
    public AddCategoryResponseDTO addCategory(AddCategoryRequestDTO request) {
        Managers existingManager = getManager();

        MenuCategory newCategory = MenuCategory.builder()
                .categoryName(request.getCategoryName())
                .categoryDescription(request.getCategoryDescription())
                .build();

        categoryRepository.save(newCategory);
        log.info("{} created a new category named {}", existingManager.getUser().getFullName(), newCategory.getCategoryName());

        return AddCategoryResponseDTO.builder()
                .categoryName(newCategory.getCategoryName())
                .message("Category is successfully created by: " + existingManager.getUser().getFullName())
                .build();
    }

    @Override
    public AddFoodItemsResponseDTO addFoodItems(AddFoodItemsRequestDTO request) {
        Managers existingManager = getManager();
        Menu existingMenu = existingManager.getBranch().getMenu();
        if(existingMenu == null) {
            throw new CreationException("Menu is not yet created for this branch. Please first create a menu instance, to add food items.");
        }

        MenuCategory existingCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Specified category does not exist."));
        FoodItems foodItem = FoodItems.builder()
                .itemName(request.getItemName())
                .itemDescription(request.getItemDescription())
                .preparationNotes(request.getPreparationNotes())
                .calories(request.getCalories())
                .category(existingCategory)
                .price(request.getPrice())
                .createdAt(LocalDate.now())
                .imageUrl(request.getImageUrl())
                .menu(existingMenu)
                .ingredients(request.getIngredients())
                .build();

        foodItemsRepository.save(foodItem);
        log.info("Added foodItem named: {}", foodItem.getItemName());

        existingCategory.getFoodItemsList().add(foodItem);
        log.info("Added the foodItem to the category's foodItems list.");

        existingMenu.getItemsList().add(foodItem);
        log.info("Added the foodItem to the branch specific menu.");

        return AddFoodItemsResponseDTO.builder()
                .foodItemName(foodItem.getItemName())
                .foodItemCategory(foodItem.getCategory().getCategoryName())
                .message("Food Item is successfully added to the menu")
                .build();
    }

    @Override
    public AddTableResponseDTO addTable(AddTableRequestDTO request) {
        Managers existingManager = getManager();

        RestaurantTables restaurantTable = RestaurantTables.builder()
                .tableNumber(request.getTableNumber())
                .seatingCapacity(request.getTableSeatingCapacity())
                .tableStatus(TableStatus.AVAILABLE)
                .branch(existingManager.getBranch())
                .createdAt(LocalDate.now())
                .build();

        tablesRepository.save(restaurantTable);
        log.info("Table is successfully created under the branch: {}", existingManager.getBranch().getBranchName());

        existingManager.getBranch().getRestaurantTablesList().add(restaurantTable);
        log.info("Added the table to the tables list of the specified branch.");

        return AddTableResponseDTO.builder()
                .addedBy(existingManager.getUser().getFullName())
                .branchName(existingManager.getBranch().getBranchName())
                .tableNumber(restaurantTable.getTableNumber())
                .build();
    }

    @Override
    public AssignmentResponseDTO assignStaff(AssignmentRequestDTO request) {
        Managers existingManager = getManager();
        Staff waiterStaff = staffRepository.findById(request.getWaiterId())
                .orElseThrow(() -> new EntityNotFoundException("Specified staff does not found. Please re-confirm the staffId."));
        if(!waiterStaff.getUser().getRole().equals(Role.WAITER_STAFF)) {
            log.error("Exception occurred: Specified staff is not waiter-staff. Tables can only be assigned to WAITER_STAFF. Please re-confirm the staffId.");
            throw new BadRequestException("Specified staff is not waiter-staff. Tables can only be assigned to WAITER_STAFF. Please re-confirm the staffId.");
        }

        Map<Long, List<Integer>> assignments = new HashMap<>();
        List<Integer> tablesList = new ArrayList<>();

        for(Integer tableId : request.getSelectedTables()) {
            RestaurantTables requestedTable = tablesRepository.findByTableNumber(tableId)
                    .orElseThrow(() -> new EntityNotFoundException("Specified tableId does not exist. Please do re-verify the selected table IDs."));
            TableAssignments newAssignment = TableAssignments.builder()
                    .temporaryAssignment(false)
                    .assignedTables(requestedTable)
                    .assignedBy(existingManager)
                    .startTime(LocalDate.now())
                    .endTime(LocalDate.now().plusDays(1))
                    .waiter(waiterStaff)
                    .build();
            tableAssignmentsRepository.save(newAssignment);

            tablesList.add(requestedTable.getTableNumber());

            requestedTable.getAssignments().add(newAssignment);
        }

        assignments.put(waiterStaff.getId(), tablesList);
        log.info("Waiter {} is assigned.", waiterStaff.getUser().getFullName());

        return AssignmentResponseDTO.builder()
                .temporaryAssignment(false)
                .assignedTables(assignments)
                .message("The waiter with ID: " + waiterStaff.getId() + " is assigned to the mentioned tables.")
                .build();

    }

    @Override
    public AssignmentResponseDTO temporaryAssignment(AssignmentRequestDTO request) {
        Managers existingManger = getManager();
        Staff waiterStaff = staffRepository.findById(request.getWaiterId())
                .orElseThrow(() -> new EntityNotFoundException("Specified waiter does not exist."));
        Map<Long, List<Integer>> assignments = new HashMap<>();
        List<Integer> tablesList = new ArrayList<>();

        for(Integer tableId : request.getSelectedTables()) {
            RestaurantTables requestedTable = tablesRepository.findByTableNumber(tableId)
                    .orElseThrow(() -> new EntityNotFoundException("Specified tableId does not exist. Please do re-verify the selected table IDs."));
            TableAssignments newAssignment = TableAssignments.builder()
                    .temporaryAssignment(true)
                    .assignedTables(requestedTable)
                    .assignedBy(existingManger)
                    .startTime(LocalDate.now())
                    .endTime(LocalDate.now().plusDays(1))
                    .waiter(waiterStaff)
                    .build();
            tableAssignmentsRepository.save(newAssignment);

            tablesList.add(requestedTable.getTableNumber());

            requestedTable.getAssignments().add(newAssignment);
        }

        assignments.put(waiterStaff.getId(), tablesList);
        log.info("Waiter {} is temporarily assigned.", waiterStaff.getUser().getFullName());

        return AssignmentResponseDTO.builder()
                .temporaryAssignment(true)
                .assignedTables(assignments)
                .message("The waiter with ID: " + waiterStaff.getId() + " is assigned to the mentioned tables.")
                .build();
    }

    @Override
    public UpdateStaffDetailsResponseDTO updateStaffDetails(UpdateStaffDetailsRequestDTO request) {
        Staff existingStaff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Specified staff does not exist"));

        Users existingUser = existingStaff.getUser();
        existingUser.setFullName(request.getFullName());
        existingUser.setUsername(request.getUsername());
        existingUser.setRole(commonService.checkStaffType(request.getStaffType()));
        existingUser.setGender(commonService.checkGender(request.getGender()));
        existingUser.setContactNumber(request.getContactNumber());
        existingUser.setAadharNumber(request.getAadharNumber());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhotoUrl(request.getPhotoUrl());

        usersRepository.save(existingUser);
        existingStaff.setUser(existingUser);
        staffRepository.save(existingStaff);

        log.info("Updated the details of {}", existingStaff.getUser().getUsername());

        return UpdateStaffDetailsResponseDTO.builder()
                .username(existingStaff.getUser().getUsername())
                .email(existingStaff.getUser().getEmail())
                .gender(existingStaff.getUser().getGender().toString())
                .aadharNumber(existingStaff.getUser().getAadharNumber())
                .photoUrl(existingStaff.getUser().getPhotoUrl())
                .fullName(existingStaff.getUser().getFullName())
                .staffType(existingStaff.getUser().getRole().toString())
                .build();
    }

    @Override
    public String deleteStaffById(Long staffId) {
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Specified staffId does not exist. Please re-confirm the entered staffId"));

        Users existingUser = existingStaff.getUser();

        usersRepository.delete(existingUser);
        log.info("UserDetails is successfully deleted.");
        staffRepository.delete(existingStaff);
        log.info("StaffDetails is successfully deleted.");

        return "Staff with staffId: " + existingStaff.getId() + " is successfully deleted by: " + getManager().getUser().getFullName();
    }

    @Override
    public List<FetchStaffDetailsResponseDTO> fetchStaffDetails(Long branchId, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Branches existingBranch = branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("Specified branch does not exist."));
        Page<Staff> allStaffDetails = staffRepository.findAllByBranch(existingBranch, pageable);
        List<Staff> listOfStaff = allStaffDetails.getContent();

        List<FetchStaffDetailsResponseDTO> response = new ArrayList<>();
        for(Staff staff : listOfStaff) {
            Users existingUser = staff.getUser();
            FetchStaffDetailsResponseDTO staffDetails = FetchStaffDetailsResponseDTO.builder()
                    .staffName(existingUser.getFullName())
                    .staffId(staff.getId())
                    .staffType(existingUser.getRole().toString())
                    .isPresent(staff.getIsActive())
                    .build();
            response.add(staffDetails);
        }

        log.info("Fetched all the details of the staff present in branch - {}", existingBranch.getId());

        return response;
    }

    public Managers getManager() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users existingUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Specified user is not found."));
        return managerRepository.findByUser(existingUser)
                .orElseThrow(() -> new EntityNotFoundException("Specified Manager is not found."));
    }
}
