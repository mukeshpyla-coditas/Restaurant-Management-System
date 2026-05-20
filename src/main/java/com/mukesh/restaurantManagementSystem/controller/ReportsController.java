package com.mukesh.restaurantManagementSystem.controller;

import com.mukesh.restaurantManagementSystem.dto.request.ViewReportsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewReportsResponseDTO;
import com.mukesh.restaurantManagementSystem.service.interfaces.DailyReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Report-Generation API")
public class ReportsController {
    private final DailyReportService dailyReportService;

    @Operation(
            summary = "Restaurant-Owner, Application Owner, and Manager can view branch specific reports.",
            description = "Owners and Managers can view the performance reports by selecting the branchId and {from, to} dates."
    )
    @GetMapping
    public ResponseEntity<ViewReportsResponseDTO> viewReports(@RequestBody @Valid ViewReportsRequestDTO request) {
        return ResponseEntity.accepted().body(dailyReportService.viewReports(request));
    }
}
