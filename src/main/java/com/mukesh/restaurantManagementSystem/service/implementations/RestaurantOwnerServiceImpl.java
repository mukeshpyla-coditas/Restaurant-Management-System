package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.dto.request.RestaurantOwnerRegisterRequestDTO;
import com.mukesh.restaurantManagementSystem.dto.response.RestaurantOwnerRegisterResponseDTO;
import com.mukesh.restaurantManagementSystem.entity.Owners;
import com.mukesh.restaurantManagementSystem.entity.Users;
import com.mukesh.restaurantManagementSystem.enums.Role;
import com.mukesh.restaurantManagementSystem.repository.RestaurantOwnerRepository;
import com.mukesh.restaurantManagementSystem.repository.UsersRepository;
import com.mukesh.restaurantManagementSystem.service.interfaces.RestaurantOwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {
    private final PasswordEncoder passwordEncoder;
    private final CommonServiceImpl commonService;
    private final UsersRepository usersRepository;
    private final RestaurantOwnerRepository restaurantOwnerRepository;

    @Override
    public RestaurantOwnerRegisterResponseDTO registerOwner(RestaurantOwnerRegisterRequestDTO request) {
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

        return RestaurantOwnerRegisterResponseDTO.builder()
                .ownerName(owner.getOwner().getFullName())
                .message("You have been successfully registered as a Restaurant Owner. " +
                        "You can now proceed to add your restaurants and respective branches.")
                .build();
    }
}
