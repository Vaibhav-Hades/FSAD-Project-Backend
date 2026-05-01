package com.agrivalueconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HomeController - Provides a simple health check endpoint
 * This controller serves the public root endpoint without requiring authentication
 */
@RestController
public class HomeController {

    /**
     * Home endpoint - Returns a simple health check message
     * Accessible at: GET http://localhost:8080/
     * No authentication required
     *
     * @return String with backend status message
     */
    @GetMapping("/")
    public String home() {
        return "Backend is running 🚀";
    }
}
