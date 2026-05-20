package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.PurchaseBillUploadRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.PurchaseBillUploadResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.StockManagementStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Stock-Management_Staff APIs")
public class StockManagementStaffController {
    private final StockManagementStaffService stockManagementStaffService;

    @Operation(
            summary = "Stock-management staff can upload the purchase-bill",
            description = "The stock-management staff can upload the purchase bills by entering the required details as per the form."
    )
    @PostMapping("/upload-bills")
    public ResponseEntity<PurchaseBillUploadResponseDTO> uploadPurchaseBills(@RequestBody @Valid PurchaseBillUploadRequestDTO request) {
        return ResponseEntity.ok(stockManagementStaffService.purchaseBillUpload(request));
    }
}
