package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.ViewReportsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewReportsResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.DailyReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class ReportsController {
    private final DailyReportService dailyReportService;

    @GetMapping
    public ResponseEntity<ViewReportsResponseDTO> viewReports(@RequestBody @Valid ViewReportsRequestDTO request) {
        return ResponseEntity.accepted().body(dailyReportService.viewReports(request));
    }
}
