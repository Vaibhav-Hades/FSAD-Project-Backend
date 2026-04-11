package com.agrivalueconnect.controller;

import com.agrivalueconnect.dto.ProductRequest;
import com.agrivalueconnect.dto.ProductResponse;
import com.agrivalueconnect.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products - public
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get product by ID - public
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Search products by keyword - public
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    // Filter by category - public
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // Get logged-in farmer's products - FARMER only
    @GetMapping("/my")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<List<ProductResponse>> getMyProducts(Principal principal) {
        return ResponseEntity.ok(productService.getProductsByFarmer(principal.getName()));
    }

    // Add new product - FARMER only
    @PostMapping
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request,
                                                       Principal principal) {
        return ResponseEntity.ok(productService.addProduct(request, principal.getName()));
    }

    // Update product - FARMER only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                          @Valid @RequestBody ProductRequest request,
                                                          Principal principal) {
        return ResponseEntity.ok(productService.updateProduct(id, request, principal.getName()));
    }

    // Delete product - FARMER or ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(id, principal.getName());
        return ResponseEntity.ok("Product deleted successfully");
    }

    // Update stock only - FARMER only
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable Long id,
                                                        @RequestParam Integer stock,
                                                        Principal principal) {
        return ResponseEntity.ok(productService.updateStock(id, stock, principal.getName()));
    }
}
