package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.PurchaseBillUploadRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.PurchaseBillUploadResponseDTO;

public interface StockManagementStaffService {
    PurchaseBillUploadResponseDTO purchaseBillUpload(PurchaseBillUploadRequestDTO request);
}
