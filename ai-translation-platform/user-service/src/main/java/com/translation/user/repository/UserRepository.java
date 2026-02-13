package com.translation.user.repository;

import com.translation.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username or email
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Check if username exists
     */
    Boolean existsByUsername(String username);

    /**
     * Check if email exists
     */
    Boolean existsByEmail(String email);
}
