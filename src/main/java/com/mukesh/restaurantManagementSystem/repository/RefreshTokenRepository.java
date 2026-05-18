package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
