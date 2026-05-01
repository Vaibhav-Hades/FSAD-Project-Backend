package com.agrivalueconnect.service;

import com.agrivalueconnect.dto.InquiryRequest;
import com.agrivalueconnect.dto.InquiryResponse;
import com.agrivalueconnect.exception.ResourceNotFoundException;
import com.agrivalueconnect.model.Inquiry;
import com.agrivalueconnect.model.User;
import com.agrivalueconnect.repository.InquiryRepository;
import com.agrivalueconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InquiryService {

    @Autowired private InquiryRepository inquiryRepository;
    @Autowired private UserRepository userRepository;

    public InquiryResponse submitInquiry(InquiryRequest request, String buyerEmail) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));

        Inquiry inquiry = new Inquiry();
        inquiry.setBuyer(buyer);
        inquiry.setProductName(request.getProductName());
        inquiry.setCategory(request.getCategory());
        inquiry.setQuantity(request.getQuantity());
        inquiry.setUnit(request.getUnit());
        inquiry.setBudget(request.getBudget());
        inquiry.setMessage(request.getMessage());
        inquiry.setDeadline(request.getDeadline());
        inquiry.setStatus("OPEN");
        return toResponse(inquiryRepository.save(inquiry));
    }

    public List<InquiryResponse> getMyInquiries(String buyerEmail) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
        return inquiryRepository.findByBuyerId(buyer.getId()).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<InquiryResponse> getAllInquiries() {
        return inquiryRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public InquiryResponse updateStatus(Long id, String status) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry", id));
        inquiry.setStatus(status);
        return toResponse(inquiryRepository.save(inquiry));
    }

    private InquiryResponse toResponse(Inquiry i) {
        InquiryResponse r = new InquiryResponse();
        r.setId(i.getId());
        r.setBuyerId(i.getBuyer().getId());
        r.setBuyerName(i.getBuyer().getName());
        r.setProductName(i.getProductName());
        r.setCategory(i.getCategory());
        r.setQuantity(i.getQuantity());
        r.setUnit(i.getUnit());
        r.setBudget(i.getBudget());
        r.setMessage(i.getMessage());
        r.setDeadline(i.getDeadline());
        r.setStatus(i.getStatus());
        r.setCreatedAt(i.getCreatedAt());
        return r;
    }
}
