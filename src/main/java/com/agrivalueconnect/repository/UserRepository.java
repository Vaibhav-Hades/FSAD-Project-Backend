package com.agrivalueconnect.repository;

import com.agrivalueconnect.model.Role;
import com.agrivalueconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login)
    Optional<User> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find all users by role
    List<User> findByRole(Role role);

    // Count users by role
    long countByRole(Role role);
}
