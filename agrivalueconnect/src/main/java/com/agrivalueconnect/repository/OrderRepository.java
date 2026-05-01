package com.agrivalueconnect.repository;

import com.agrivalueconnect.model.Order;
import com.agrivalueconnect.model.OrderStatus;
import com.agrivalueconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Order entity
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get all orders by a specific buyer
    List<Order> findByBuyer(User buyer);

    // Get all orders by buyer ID
    List<Order> findByBuyerId(Long buyerId);

    // Get all orders for a specific product
    List<Order> findByProductId(Long productId);

    // Get orders by status
    List<Order> findByStatus(OrderStatus status);

    // Count orders by status
    long countByStatus(OrderStatus status);

    // Get orders for products owned by a specific farmer
    List<Order> findByProductFarmerId(Long farmerId);
}
