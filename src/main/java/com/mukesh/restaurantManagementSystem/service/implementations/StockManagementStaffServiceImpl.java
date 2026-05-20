package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.PurchaseBillUploadRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.PurchaseBillUploadResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.PurchaseBills;
import com.mukesh.restaurantManagementSystem.entity.Staff;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.exceptions.InvalidTypeException;
import com.mukesh.restaurantManagementSystem.repository.PurchaseBillsRepository;
import com.mukesh.restaurantManagementSystem.repository.StaffRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.StockManagementStaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockManagementStaffServiceImpl implements StockManagementStaffService {
    private final PurchaseBillsRepository purchaseBillsRepository;
    private final UsersRepository usersRepository;
    private final StaffRepository staffRepository;

    @Override
    public PurchaseBillUploadResponseDTO purchaseBillUpload(PurchaseBillUploadRequestDTO request) {
        Staff existingStockManagementStaff = getStaff();
        if(!existingStockManagementStaff.getUser().getRole().equals(Role.STOCK_MANAGEMENT_STAFF)) {
            throw new InvalidTypeException("Specified user is not stockManagementStaff/manager. Please re-confirm the role of user.");
        }

        PurchaseBills purchaseBills = PurchaseBills.builder()
                .purchaseDate(request.getPurchaseDate())
                .stockManagementStaff(existingStockManagementStaff)
                .billUrl(request.getBillUrl())
                .totalPurchaseAmount(request.getTotalPurchaseAmount())
                .build();

        purchaseBillsRepository.save(purchaseBills);
        log.info("Purchase Bill has been uploaded by: {}", existingStockManagementStaff.getUser().getFullName());

        return PurchaseBillUploadResponseDTO.builder()
                .uploadedBy(existingStockManagementStaff.getUser().getUsername())
                .totalPurchaseAmount(purchaseBills.getTotalPurchaseAmount())
                .uploadedAt(LocalDate.now())
                .build();
    }

    public Staff getStaff() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Specified user does not exist."));
        return staffRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Specified staff does not exist."));
    }
}
