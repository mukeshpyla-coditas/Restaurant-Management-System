package com.mukesh.restaurantManagementSystem.repository;

import com.mukesh.restaurantManagementSystem.entity.Branches;
import com.mukesh.restaurantManagementSystem.entity.Staff;
import com.mukesh.restaurantManagementSystem.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUser(Users user);
    Page<Staff> findAllByBranch(Branches branch, Pageable pageable);
}
