package com.agrivalueconnect.repository;

import com.agrivalueconnect.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Inquiry entity
 */
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // Get all inquiries by a specific buyer
    List<Inquiry> findByBuyerId(Long buyerId);

    // Get inquiries by status
    List<Inquiry> findByStatus(String status);
}
