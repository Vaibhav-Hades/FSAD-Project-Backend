package com.agrivalueconnect.controller;

import com.agrivalueconnect.dto.InquiryRequest;
import com.agrivalueconnect.dto.InquiryResponse;
import com.agrivalueconnect.service.InquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // Submit a bulk inquiry - BUYER only
    @PostMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<InquiryResponse> submitInquiry(@Valid @RequestBody InquiryRequest request,
                                                          Principal principal) {
        return ResponseEntity.ok(inquiryService.submitInquiry(request, principal.getName()));
    }

    // Get logged-in buyer's inquiries - BUYER only
    @GetMapping("/my")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<InquiryResponse>> getMyInquiries(Principal principal) {
        return ResponseEntity.ok(inquiryService.getMyInquiries(principal.getName()));
    }

    // Get all inquiries - ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InquiryResponse>> getAllInquiries() {
        return ResponseEntity.ok(inquiryService.getAllInquiries());
    }

    // Update inquiry status - ADMIN only
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InquiryResponse> updateStatus(@PathVariable Long id,
                                                         @RequestParam String status) {
        return ResponseEntity.ok(inquiryService.updateStatus(id, status));
    }
}
