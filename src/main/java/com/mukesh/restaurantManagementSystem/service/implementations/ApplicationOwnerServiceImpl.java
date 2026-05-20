package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.ApplicationOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchBranchDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.FetchRestaurantDetailsResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Invitation;
import com.mukesh.restaurantManagementSystem.entity.Restaurants;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.InviteStatus;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.repository.InvitationRepository;
import com.mukesh.restaurantManagementSystem.repository.RefreshTokenRepository;
import com.mukesh.restaurantManagementSystem.repository.RestaurantRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.ApplicationOwnerService;
import com.mukesh.restaurantManagementSystem.service.interfaces.RestaurantOwnerService;
import com.mukesh.restaurantManagementSystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationOwnerServiceImpl implements ApplicationOwnerService {
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final RestaurantRepository restaurantRepository;
    private final CommonServiceImpl commonService;
    private final RestaurantOwnerService restaurantOwnerService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final InvitationRepository invitationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RestaurantOwnerInviteResponseDTO inviteUser(RestaurantOwnerInviteRequestDTO request) {
        Users sender = usersRepository.findById(request.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("User specified does not exist"));

        String inviteCode = UUID.randomUUID().toString();
        String apiCall = "/v1/auth/restaurant-owner/register/" + inviteCode;
        commonService.sendMail(request, sender, apiCall);
        log.info("Mail has been sent to the restaurant-owner. Receiver Mail: {}", request.getReceiverEmail());
        
        Invitation invitation = Invitation.builder()
                .inviteCode(inviteCode)
                .issuedAt(LocalDate.now())
                .expirationTime(LocalDate.now().plusDays(1))
                .inviteStatus(InviteStatus.PENDING)
                .sentBy(sender)
                .build();
        invitationRepository.save(invitation);
        log.info("Saved the invite token into the DB along with the expiry.");

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
    public FetchRestaurantDetailsResponseDTO fetchRestaurantDetails(Long restaurantId, Integer size, Integer page) {
        Restaurants requestedRestaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Specified restaurant is not found. Please re-confirm the restaurant ID."));

        List<FetchBranchDetailsResponseDTO> branchDetails = restaurantOwnerService.fetchBranchDetails(restaurantId, size, page);

        log.info("Fetched the details of every branch, of the specified restaurantId - {}", restaurantId);
        return FetchRestaurantDetailsResponseDTO.builder()
                .ownerName(requestedRestaurant.getOwner().getOwner().getFullName())
                .restaurantId(requestedRestaurant.getId())
                .restaurantName(requestedRestaurant.getRestaurantName())
                .restaurantCreatedAt(requestedRestaurant.getCreatedAt())
                .branchDetails(branchDetails)
                .build();

    }
}
