package com.delivery.products.controller;

import com.delivery.products.dto.ProductCreateRequest;
import com.delivery.products.dto.ProductResponse;
import com.delivery.products.dto.ProductUpdateRequest;
import com.delivery.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "Product management API")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all active products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID") @PathVariable Integer id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Retrieve products containing the specified name")
    public ResponseEntity<List<ProductResponse>> getProductsByName(
            @Parameter(description = "Product name to search") @RequestParam String name) {
        List<ProductResponse> products = productService.getProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Product to create") @Valid @RequestBody ProductCreateRequest product) {
        try {
            ProductResponse createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update an existing product with the provided details")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Integer id,
            @Parameter(description = "Updated product details") @Valid @RequestBody ProductUpdateRequest product) {
        try {
            ProductResponse updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Soft delete a product by its ID")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if product exists", description = "Check if a product with the given ID exists")
    public ResponseEntity<Boolean> productExists(
            @Parameter(description = "Product ID") @PathVariable Integer id) {
        boolean exists = productService.productExists(id);
        return ResponseEntity.ok(exists);
    }
}
