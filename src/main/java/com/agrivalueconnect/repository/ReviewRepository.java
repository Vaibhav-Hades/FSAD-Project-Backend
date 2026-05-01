package com.agrivalueconnect.repository;

import com.agrivalueconnect.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Review entity
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Get all reviews for a specific product
    List<Review> findByProductId(Long productId);

    // Get all reviews by a specific user
    List<Review> findByUserId(Long userId);

    // Check if user already reviewed a product
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
