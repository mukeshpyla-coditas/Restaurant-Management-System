package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.UserInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.UserInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Invitation;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.InviteStatus;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.repository.InvitationRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JavaMailSender javaMailSender;
    private final UsersRepository usersRepository;
    private final InvitationRepository invitationRepository;

    @Override
    public UserInviteResponseDTO inviteUser(UserInviteRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users sender = usersRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User specified does not exist"));

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(request.getReceiverEmail());
        simpleMailMessage.setSubject("Invite to onboard onto the Management Application");
        simpleMailMessage.setFrom(sender.getEmail());
        simpleMailMessage.setText("Please NOTE that the link will be active until next 12hrs. Please do register before the expiry. Thank you!");
        javaMailSender.send(simpleMailMessage);

        String inviteCode = UUID.randomUUID().toString();
        Invitation invitation = Invitation.builder()
                .inviteCode(inviteCode)
                .issuedAt(LocalDate.now())
                .expirationTime(LocalDate.now().plusDays(1))
                .inviteStatus(InviteStatus.PENDING)
                .sentBy(sender)
                .build();
        invitationRepository.save(invitation);

        return UserInviteResponseDTO.builder()
                .sentTo(request.getReceiverEmail())
                .message("Mail is successfully sent")
                .build();
    }
}
