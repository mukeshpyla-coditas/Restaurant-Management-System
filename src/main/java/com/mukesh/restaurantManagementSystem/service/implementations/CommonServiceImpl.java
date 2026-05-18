package com.mukesh.restaurantManagementSystem.service.implementations;

import com.mukesh.restaurantManagementSystem.enums.Gender;
import com.mukesh.restaurantManagementSystem.exceptions.InvalidTypeException;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl {
    public Gender checkGender(String requestedGender) {
        for(Gender gender : Gender.values()) {
            if(gender.name().equals(requestedGender.toUpperCase())) return gender;
        }

        throw new InvalidTypeException("Please enter valid restaurant type: ['LUXURY', 'GENERAL']");
    }


}
