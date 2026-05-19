package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.PurchaseBillUploadRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.PurchaseBillUploadResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.StockManagementStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stock-management-staff")
@RequiredArgsConstructor
public class StockManagementStaffController {
    private final StockManagementStaffService stockManagementStaffService;

    @PostMapping("/upload-bills")
    public ResponseEntity<PurchaseBillUploadResponseDTO> uploadPurchaseBills(@RequestBody @Valid PurchaseBillUploadRequestDTO request) {
        return ResponseEntity.ok(stockManagementStaffService.purchaseBillUpload(request));
    }
}
