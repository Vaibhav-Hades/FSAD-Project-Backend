package com.agrivalueconnect.service;

import com.agrivalueconnect.dto.ProductRequest;
import com.agrivalueconnect.dto.ProductResponse;
import com.agrivalueconnect.exception.BadRequestException;
import com.agrivalueconnect.exception.ResourceNotFoundException;
import com.agrivalueconnect.model.Product;
import com.agrivalueconnect.model.Role;
import com.agrivalueconnect.model.User;
import com.agrivalueconnect.repository.ProductRepository;
import com.agrivalueconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        return toResponse(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id)));
    }

    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByFarmer(String farmerEmail) {
        User farmer = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
        return productRepository.findByFarmer(farmer).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProductResponse addProduct(ProductRequest request, String farmerEmail) {
        User farmer = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
        if (farmer.getRole() != Role.FARMER) {
            throw new BadRequestException("Only farmers can add products");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setStock(request.getStock());
        product.setMoq(request.getMoq());
        product.setBadges(request.getBadges());
        product.setImageUrl(request.getImageUrl());
        product.setFarmer(farmer);
        return toResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request, String farmerEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        if (!product.getFarmer().getEmail().equals(farmerEmail)) {
            throw new BadRequestException("You can only update your own products");
        }
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setStock(request.getStock());
        product.setMoq(request.getMoq());
        product.setBadges(request.getBadges());
        product.setImageUrl(request.getImageUrl());
        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id, String userEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() != Role.ADMIN && !product.getFarmer().getEmail().equals(userEmail)) {
            throw new BadRequestException("You can only delete your own products");
        }
        productRepository.delete(product);
    }

    public ProductResponse updateStock(Long id, Integer newStock, String farmerEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        if (!product.getFarmer().getEmail().equals(farmerEmail)) {
            throw new BadRequestException("You can only update your own products");
        }
        product.setStock(newStock);
        return toResponse(productRepository.save(product));
    }

    private ProductResponse toResponse(Product p) {
        ProductResponse r = new ProductResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setDescription(p.getDescription());
        r.setCategory(p.getCategory());
        r.setPrice(p.getPrice());
        r.setUnit(p.getUnit());
        r.setStock(p.getStock());
        r.setMoq(p.getMoq());
        r.setBadges(p.getBadges());
        r.setImageUrl(p.getImageUrl());
        r.setFarmerId(p.getFarmer().getId());
        r.setFarmerName(p.getFarmer().getName());
        r.setFarmerVillage(p.getFarmer().getVillage());
        return r;
    }
}
