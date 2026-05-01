package com.agrivalueconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AgriValue Connect - Spring Boot Backend Application
 *
 * Roles: ADMIN, FARMER, BUYER
 * Features: JWT Auth, Product Management, Orders, Reviews, Inquiries
 *
 * Run: mvn spring-boot:run
 * API Base URL: http://localhost:8080/api
 */
@SpringBootApplication
public class AgriValueConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriValueConnectApplication.class, args);
    }
}
