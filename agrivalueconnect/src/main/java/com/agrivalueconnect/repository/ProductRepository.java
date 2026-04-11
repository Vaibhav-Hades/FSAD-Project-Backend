package com.agrivalueconnect.repository;

import com.agrivalueconnect.model.Product;
import com.agrivalueconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Product entity
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Get all products by a specific farmer
    List<Product> findByFarmer(User farmer);

    // Get all products by farmer ID
    List<Product> findByFarmerId(Long farmerId);

    // Search products by category
    List<Product> findByCategory(String category);

    // Search products by name containing keyword
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Count products by farmer
    long countByFarmer(User farmer);
}
