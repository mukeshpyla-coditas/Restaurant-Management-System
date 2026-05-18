package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.ApplicationOwnerLoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.ApplicationOwnerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.UserInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerLoginResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.UserInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Invitation;
import com.mukesh.restaurantManagementSystem.entity.RefreshToken;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.InviteStatus;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.repository.InvitationRepository;
import com.mukesh.restaurantManagementSystem.repository.RefreshTokenRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.ApplicationOwnerService;
import com.mukesh.restaurantManagementSystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationOwnerServiceImpl implements ApplicationOwnerService {
    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender;
    private final UsersRepository usersRepository;
    private final CommonServiceImpl commonService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final InvitationRepository invitationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserInviteResponseDTO inviteUser(UserInviteRequestDTO request) {
        Users sender = usersRepository.findById(request.getSenderId()).orElseThrow(() -> new EntityNotFoundException("User specified does not exist"));

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

    @Override
    public ApplicationOwnerRegisterResponseDTO registerApplicationOwner(ApplicationOwnerRegisterRequestDTO request) {
        Users applicationOwner = Users.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .aadharNumber(request.getAadharNumber())
                .contactNumber(request.getContactNumber())
                .isActive(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .joinedAt(LocalDate.now())
                .photoUrl(request.getPhotoUrl())
                .gender(commonService.checkGender(request.getGender()))
                .role(Role.APPLICATION_OWNER)
                .build();

        usersRepository.save(applicationOwner);
        log.info("Application Owner {} is successfully created.", applicationOwner.getFullName());

        return ApplicationOwnerRegisterResponseDTO.builder()
                .ownerName(applicationOwner.getFullName())
                .message("You are successfully registered as Application Owner. You can now proceed to add Restaurant Owners.")
                .build();
    }

    @Override
    public ApplicationOwnerLoginResponseDTO loginApplicationOwner(ApplicationOwnerLoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String accessToken = jwtUtil.generateToken(request.getUsername(), Role.APPLICATION_OWNER.toString());
            String refreshToken = UUID.randomUUID().toString();
            RefreshToken refreshToken1 = RefreshToken.builder()
                    .refreshToken(refreshToken)
                    .user(usersRepository.findByUsername(request.getUsername()).orElseThrow(() -> new EntityNotFoundException("Specified user is not found.")))
                    .createdAt(LocalDate.now())
                    .expirationAt(LocalDate.now().plusDays(1))
                    .build();
            refreshTokenRepository.save(refreshToken1);

            return ApplicationOwnerLoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .message("The token will be active for next 10min.")
                    .build();
        } catch (Exception exception) {
            log.error("Exception occurred: {}", exception.getMessage());
        }

        return null;
    }
}
