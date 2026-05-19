package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.ViewReportsRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ReportResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ViewReportsResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Branches;
import com.mukesh.restaurantManagementSystem.entity.DailyReports;
import com.mukesh.restaurantManagementSystem.entity.Managers;
import com.mukesh.restaurantManagementSystem.entity.Owners;
import com.mukesh.restaurantManagementSystem.repository.BillsRepository;
import com.mukesh.restaurantManagementSystem.repository.BranchRepository;
import com.mukesh.restaurantManagementSystem.repository.DailyReportRepository;
import com.mukesh.restaurantManagementSystem.repository.PurchaseBillsRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.DailyReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyReportServiceImpl implements DailyReportService {
    private final BillsRepository billsRepository;
    private final PurchaseBillsRepository purchaseBillsRepository;
    private final DailyReportRepository dailyReportRepository;
    private final BranchRepository branchRepository;
    private final CommonServiceImpl commonService;

    @Override
    @Scheduled(cron = "0 0 5 * * *")
    public void generateDailyReport() {
        List<Branches> branchesList = branchRepository.findAll();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        for(Branches branch : branchesList) {
            Double totalIncome = billsRepository.getDailyIncome(branch.getId(), yesterday);
            Double totalExpenditure = purchaseBillsRepository.getDailyExpenditure(branch.getId(), yesterday);
            Integer totalOrders = billsRepository.getTotalOrders(branch.getId(), yesterday);
            Double totalProfit = totalIncome - totalExpenditure;
            Double profitPercentage = 0.0;
            if(totalIncome > 0) {
                profitPercentage = (totalProfit / totalIncome) * 100;
            }

            DailyReports dailyReports = DailyReports.builder()
                    .branchId(branch.getId())
                    .totalIncome(totalIncome)
                    .totalExpenditure(totalExpenditure)
                    .totalOrders(totalOrders)
                    .totalProfit(totalProfit)
                    .profitPercentage(profitPercentage)
                    .reportDate(yesterday)
                    .generatedAt(LocalDateTime.now())
                    .build();

            dailyReportRepository.save(dailyReports);
            log.info("Saved the daily report for the date: {}", dailyReports.getReportDate());

            Managers branchManager = branch.getManager();
            commonService.sendMail(dailyReports, branchManager.getUser().getEmail());
            log.info("Sent mail to the manager: {}", branchManager.getUser().getFullName());
            Owners restaurantOwner = branch.getRestaurant().getOwner();
            commonService.sendMail(dailyReports, restaurantOwner.getOwner().getEmail());
            log.info("Sent mail to the restaurant owner: {}", restaurantOwner.getOwner().getFullName());
        }
    }

    @Override
    public ViewReportsResponseDTO viewReports(ViewReportsRequestDTO request) {
        ReportResponseDTO response = dailyReportRepository.findReportsBetweenFromAndTo(request.getBranchId(), request.getFrom(), request.getTo());
        Double profitPercentage = 0.0;
        if(response.getTotalIncome() > 0.0) {
            profitPercentage = (response.getTotalProfit() / response.getTotalIncome()) * 100.0;
        }
        return ViewReportsResponseDTO.builder()
                .branchId(request.getBranchId())
                .totalIncome(response.getTotalIncome())
                .totalExpenditure(response.getTotalExpenditure())
                .totalProfit(response.getTotalProfit())
                .profitPercentage(profitPercentage)
                .totalOrders(response.getTotalOrders())
                .build();
    }
}
