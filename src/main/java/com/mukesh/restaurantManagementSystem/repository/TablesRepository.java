package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.RestaurantTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TablesRepository extends JpaRepository<RestaurantTables,Long> {
    Optional<RestaurantTables> findByTableNumber(Integer tableNumber);
}
