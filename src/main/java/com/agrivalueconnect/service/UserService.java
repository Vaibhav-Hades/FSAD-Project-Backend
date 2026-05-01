package com.agrivalueconnect.service;

import com.agrivalueconnect.dto.AnalyticsResponse;
import com.agrivalueconnect.dto.UserResponse;
import com.agrivalueconnect.exception.BadRequestException;
import com.agrivalueconnect.exception.ResourceNotFoundException;
import com.agrivalueconnect.model.OrderStatus;
import com.agrivalueconnect.model.Role;
import com.agrivalueconnect.model.User;
import com.agrivalueconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private InquiryRepository inquiryRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByRole(String role) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            return userRepository.findByRole(userRole).stream().map(this::toResponse).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + role);
        }
    }

    public UserResponse getUserById(Long id) {
        return toResponse(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.delete(user);
    }

    public AnalyticsResponse getAnalytics() {
        AnalyticsResponse r = new AnalyticsResponse();
        r.setTotalUsers(userRepository.count());
        r.setTotalFarmers(userRepository.countByRole(Role.FARMER));
        r.setTotalBuyers(userRepository.countByRole(Role.BUYER));
        r.setTotalProducts(productRepository.count());
        r.setTotalOrders(orderRepository.count());
        r.setTotalReviews(reviewRepository.count());
        r.setTotalInquiries(inquiryRepository.count());
        r.setPendingOrders(orderRepository.countByStatus(OrderStatus.PENDING));
        r.setDeliveredOrders(orderRepository.countByStatus(OrderStatus.DELIVERED));
        return r;
    }

    private UserResponse toResponse(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setName(u.getName());
        r.setEmail(u.getEmail());
        r.setRole(u.getRole().name());
        r.setVillage(u.getVillage());
        r.setPhone(u.getPhone());
        r.setSpecialty(u.getSpecialty());
        return r;
    }
}
