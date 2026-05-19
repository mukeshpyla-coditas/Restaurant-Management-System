package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.BranchManagerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.entity.DailyReports;
import com.mukesh.restaurantManagementSystem.entity.Invitation;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.Gender;
import com.mukesh.restaurantManagementSystem.enums.InviteStatus;
import com.mukesh.restaurantManagementSystem.enums.RestaurantType;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.exceptions.InvalidTypeException;
import com.mukesh.restaurantManagementSystem.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl {
    private final JavaMailSender javaMailSender;
    private final InvitationRepository invitationRepository;

    public Gender checkGender(String requestedGender) {
        for(Gender gender : Gender.values()) {
            if(gender.name().equals(requestedGender.toUpperCase())) return gender;
        }

        throw new InvalidTypeException("Please enter valid gender: ['MALE', 'FEMALE', 'OTHERS']");
    }

    public RestaurantType checkRestaurantType(String requestedRestaurantType) {
        for(RestaurantType restaurantType : RestaurantType.values()) {
            if(restaurantType.name().equals(requestedRestaurantType.toUpperCase())) return restaurantType;
        }

        throw new InvalidTypeException("Please enter valid restaurant type: ['LUXURY', 'GENERAL']");
    }

    public Role checkStaffType(String requestedStaffType) {
        for(Role staffType : Role.values()) {
            if(staffType.name().equals(requestedStaffType.toUpperCase())) return staffType;
        }

        throw new InvalidTypeException("Please enter valid staffType: ['WAITER_STAFF', 'COOKING_STAFF', 'STOCK_MANAGEMENT_STAFF']");
    }

    public void sendMail(RestaurantOwnerInviteRequestDTO request, Users sender, String apiCall) {
        String restaurantOwnerRegistrationLink = "https://playtime-sanitary-nutcase.ngrok-free.dev" + apiCall;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(request.getReceiverEmail());
        simpleMailMessage.setSubject("Invite to onboard onto the Management Application");
        simpleMailMessage.setFrom(sender.getEmail());
        simpleMailMessage.setText("Link: " + restaurantOwnerRegistrationLink +"\nPlease NOTE that the link will be active until next 12hrs. Please do register before the expiry. Thank you!");
        javaMailSender.send(simpleMailMessage);
    }

    public void sendMail(BranchManagerInviteRequestDTO request, Users sender, String apiCall) {
        String restaurantOwnerRegistrationLink = "https://playtime-sanitary-nutcase.ngrok-free.dev" + apiCall;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(request.getReceiverMail());
        simpleMailMessage.setSubject("Invite to onboard onto the Management Application");
        simpleMailMessage.setFrom(sender.getEmail());
        simpleMailMessage.setText("Link: " + restaurantOwnerRegistrationLink +"\nBranchId: " + request.getBranchId() + "\nPlease NOTE that the link will be active until next 12hrs. Please do register before the expiry. Thank you!");
        javaMailSender.send(simpleMailMessage);
    }

    public void sendMail(DailyReports request, String receiverMail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("mukesh.pyla@coditas.com");
        simpleMailMessage.setTo(receiverMail);
        simpleMailMessage.setSubject("Daily Report of Branch");
        simpleMailMessage.setText("BranchId: " + request.getBranchId() + "\nTotalIncome: " + request.getTotalIncome()
        + "\nTotalExpenditure: " + request.getTotalExpenditure() + "\nTotalOrders: " + request.getTotalOrders() + "\nTotalProfit: " + request.getTotalProfit()
        + "\nProfitPercentage: " + request.getProfitPercentage() + "\nReportDate: " + request.getReportDate()
        + "\n\nThis is the daily report of branch: " + request.getBranchId());
        javaMailSender.send(simpleMailMessage);
    }

    public boolean isInviteTokenValid(String inviteToken) {
        Invitation invitation = invitationRepository.findByInviteCode(inviteToken);
        return invitation.getExpirationTime().isAfter(LocalDate.now()) && invitation.getInviteStatus() == InviteStatus.PENDING;
    }

}
