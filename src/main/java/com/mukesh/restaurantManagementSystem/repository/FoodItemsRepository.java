package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.FoodItems;
import com.mukesh.restaurantManagementSystem.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemsRepository extends JpaRepository<FoodItems, Long> {
    List<FoodItems> findByMenu(Menu menu);
}
