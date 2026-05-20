package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.Branches;
import com.mukesh.restaurantManagementSystem.entity.Restaurants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branches, Long> {
    Page<Branches> findByRestaurant(Restaurants restaurant, Pageable pageable);
}
