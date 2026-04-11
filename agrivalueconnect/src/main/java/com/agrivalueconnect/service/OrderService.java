package com.agrivalueconnect.service;

import com.agrivalueconnect.dto.OrderRequest;
import com.agrivalueconnect.dto.OrderResponse;
import com.agrivalueconnect.exception.BadRequestException;
import com.agrivalueconnect.exception.ResourceNotFoundException;
import com.agrivalueconnect.model.*;
import com.agrivalueconnect.repository.OrderRepository;
import com.agrivalueconnect.repository.ProductRepository;
import com.agrivalueconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    public OrderResponse placeOrder(OrderRequest request, String buyerEmail) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.getProductId()));

        if (product.getStock() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock. Available: " + product.getStock());
        }
        if (product.getMoq() != null && request.getQuantity() < product.getMoq()) {
            throw new BadRequestException("Minimum order quantity is " + product.getMoq() + " " + product.getUnit());
        }

        double totalPrice = product.getPrice() * request.getQuantity();
        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        Order order = new Order();
        order.setBuyer(buyer);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.PENDING);
        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getMyOrders(String buyerEmail) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
        return orderRepository.findByBuyer(buyer).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OrderResponse> getFarmerOrders(String farmerEmail) {
        User farmer = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
        return orderRepository.findByProductFarmerId(farmer.getId()).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        try {
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status);
        }
        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(Order o) {
        OrderResponse r = new OrderResponse();
        r.setId(o.getId());
        r.setProductId(o.getProduct().getId());
        r.setProductName(o.getProduct().getName());
        r.setBuyerId(o.getBuyer().getId());
        r.setBuyerName(o.getBuyer().getName());
        r.setQuantity(o.getQuantity());
        r.setTotalPrice(o.getTotalPrice());
        r.setStatus(o.getStatus().name());
        r.setCreatedAt(o.getCreatedAt());
        return r;
    }
}
