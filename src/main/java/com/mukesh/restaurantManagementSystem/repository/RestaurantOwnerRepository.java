package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.Owners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantOwnerRepository extends JpaRepository<Owners, Long> {
}
