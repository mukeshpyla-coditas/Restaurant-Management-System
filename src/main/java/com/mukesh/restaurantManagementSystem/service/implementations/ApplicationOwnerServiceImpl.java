package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.LoginRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.LoginResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerInviteResponseDTO;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationOwnerServiceImpl implements ApplicationOwnerService {
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final CommonServiceImpl commonService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final InvitationRepository invitationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RestaurantOwnerInviteResponseDTO inviteUser(RestaurantOwnerInviteRequestDTO request) {
        Users sender = usersRepository.findById(request.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("User specified does not exist"));

        String inviteCode = UUID.randomUUID().toString();
        String apiCall = "/v1/restaurant-owner/register/" + inviteCode;
        commonService.sendMail(request, sender, apiCall);
        
        Invitation invitation = Invitation.builder()
                .inviteCode(inviteCode)
                .issuedAt(LocalDate.now())
                .expirationTime(LocalDate.now().plusDays(1))
                .inviteStatus(InviteStatus.PENDING)
                .sentBy(sender)
                .build();
        invitationRepository.save(invitation);

        return RestaurantOwnerInviteResponseDTO.builder()
                .sentTo(request.getReceiverEmail())
                .message("Mail is successfully sent")
                .build();
    }

    @Override
    public ApplicationOwnerRegisterResponseDTO registerApplicationOwner(RegisterRequestDTO request) {
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
    public LoginResponseDTO loginApplicationOwner(LoginRequestDTO request) {
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

            return LoginResponseDTO.builder()
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
