package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.BranchManagerInviteRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.BranchRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.request.RestaurantRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.BranchManagerInviteResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.BranchRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Branches;
import com.mukesh.restaurantManagementSystem.entity.Invitation;
import com.mukesh.restaurantManagementSystem.entity.Managers;
import com.mukesh.restaurantManagementSystem.entity.Owners;
import com.mukesh.restaurantManagementSystem.entity.Restaurants;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.InviteStatus;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.exceptions.CodeExpiredException;
import com.mukesh.restaurantManagementSystem.exceptions.EntityNotFoundException;
import com.mukesh.restaurantManagementSystem.repository.BranchRepository;
import com.mukesh.restaurantManagementSystem.repository.InvitationRepository;
import com.mukesh.restaurantManagementSystem.repository.RestaurantOwnerRepository;
import com.mukesh.restaurantManagementSystem.repository.RestaurantRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.RestaurantOwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {
    private final PasswordEncoder passwordEncoder;
    private final CommonServiceImpl commonService;
    private final UsersRepository usersRepository;
    private final BranchRepository branchRepository;
    private final InvitationRepository invitationRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantOwnerRepository restaurantOwnerRepository;

    @Override
    public RestaurantOwnerRegisterResponseDTO registerOwner(String inviteCode, RegisterRequestDTO request) {
        if(!commonService.isInviteTokenValid(inviteCode)) {
            throw new CodeExpiredException("InviteCode is expired. Please wait for the next invite mail.");
        }

        Users newUser = Users.builder()
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
                .role(Role.RESTAURANT_OWNER)
                .build();

        usersRepository.save(newUser);
        log.info("New user has been successfully registered!");

        Owners owner = Owners.builder().owner(newUser).build();
        restaurantOwnerRepository.save(owner);
        log.info("New restaurant owner has been successfully registered!");

        Invitation acticeInvitation = invitationRepository.findByInviteCode(inviteCode);
        acticeInvitation.setInviteStatus(InviteStatus.ACCEPTED);
        invitationRepository.save(acticeInvitation);

        return RestaurantOwnerRegisterResponseDTO.builder()
                .ownerName(owner.getOwner().getFullName())
                .message("You have been successfully registered as a Restaurant Owner. " +
                        "You can now proceed to add your restaurants and respective branches.")
                .build();
    }

    @Override
    public RestaurantRegisterResponseDTO registerRestaurant(RestaurantRegisterRequestDTO request) {
        Owners existingOwner = restaurantOwnerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Specified RestaurantOwner is not found. " +
                        "Please register as a RestaurantOwner, to be able to register a restaurant."));

        Restaurants restaurant = Restaurants.builder()
                .owner(existingOwner)
                .restaurantName(request.getRestaurantName())
                .restaurantType(commonService.checkRestaurantType(request.getRestaurantType()))
                .createdAt(LocalDate.now())
                .build();

        restaurantRepository.save(restaurant);
        log.info("Created a new Restaurant Chain under the RestaurantOwner: {}", existingOwner.getOwner().getFullName());

        existingOwner.getRestaurantsList().add(restaurant);
        log.info("Added teh newly created restaurant to the {} list of restaurants.", existingOwner.getOwner().getFullName());

        return RestaurantRegisterResponseDTO.builder()
                .ownerName(existingOwner.getOwner().getFullName())
                .restaurantName(restaurant.getRestaurantName())
                .message("RestaurantChain is successfully created. Now you can proceed to adding branches to this chain of restaurants.")
                .build();
    }

    @Override
    public BranchRegisterResponseDTO registerBranch(BranchRegisterRequestDTO request) {
        Restaurants existingRestaurant = restaurantRepository.findById(request.getRestaurantChainId())
                .orElseThrow(() -> new EntityNotFoundException("Specified restaurant is not found. Please do register the RestaurantChain first, to register the branch."));

        Branches branch = Branches.builder()
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .seatingCapacity(request.getSeatingCapacity())
                .contactNumber(request.getContactNumber())
                .restaurant(existingRestaurant)
                .createdAt(LocalDate.now())
                .address(request.getAddress())
                .build();

        branchRepository.save(branch);
        log.info("Branch named {} is successfully created.", branch.getBranchName());

        existingRestaurant.getBranchesList().add(branch);
        log.info("Branch added to the existing restaurant chain {}'s branches list.", existingRestaurant.getRestaurantName());

        return BranchRegisterResponseDTO.builder()
                .restaurantOwner(existingRestaurant.getOwner().getOwner().getFullName())
                .restaurantChainName(existingRestaurant.getRestaurantName())
                .branchName(branch.getBranchName())
                .message("Branch is successfully created. User can now proceed to assign branch manager to created branch.")
                .build();

    }

    @Override
    public BranchManagerInviteResponseDTO inviteBranchManager(BranchManagerInviteRequestDTO request) {
        Owners sender = restaurantOwnerRepository.findById(request.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("Specified user does not exist. Please re-verify the senderId."));
        String inviteCode = UUID.randomUUID().toString();
        String apiCall = "/v1/branch-manager/register/" + inviteCode;
        commonService.sendMail(request, sender.getOwner(), apiCall);
        log.info("Mail is successfully sent to: {}", request.getReceiverMail());

        Invitation invitation = Invitation.builder()
                .inviteCode(inviteCode)
                .issuedAt(LocalDate.now())
                .expirationTime(LocalDate.now().plusDays(1))
                .inviteStatus(InviteStatus.PENDING)
                .sentBy(sender.getOwner())
                .build();
        invitationRepository.save(invitation);
        log.info("Invite details is successfully saved and persisted. Invite Code: {}", invitation.getInviteCode());

        return BranchManagerInviteResponseDTO.builder()
                .sentBy(sender.getOwner().getEmail())
                .sentTo(request.getReceiverMail())
                .message("Invite is successfully sent to the specified email. You can keep a track of invites through the 'invites' tab.")
                .build();
    }
}
