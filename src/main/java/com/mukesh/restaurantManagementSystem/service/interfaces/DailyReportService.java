package com.mukesh.restaurantManagementSystem.service.interfaces;

import com.mukesh.restaurantManagementSystem.dto.request.ViewReportsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewReportsResponseDTO;

public interface DailyReportService {
    void generateDailyReport();
    ViewReportsResponseDTO viewReports(ViewReportsRequestDTO request);
}
