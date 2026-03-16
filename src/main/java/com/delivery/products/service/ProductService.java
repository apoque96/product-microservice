package com.delivery.products.service;

import com.delivery.products.dto.ProductCreateRequest;
import com.delivery.products.dto.ProductResponse;
import com.delivery.products.dto.ProductUpdateRequest;
import com.delivery.products.entity.Product;
import com.delivery.products.mapper.ProductMapper;
import com.delivery.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all active products");
        List<ProductResponse> ans = new ArrayList<>();
        productRepository.findAllActive().forEach(product ->
                ans.add(ProductMapper.mapProductToProductResponse(product, new ProductResponse())));
        return ans;
    }

    public ProductResponse getProductById(Integer id) {
        log.info("Fetching product with id: {}", id);
        var product = productRepository.findActiveById(id).orElseThrow(() -> new RuntimeException("No product found with id: " + id));
        return ProductMapper.mapProductToProductResponse(product, new ProductResponse());
    }

    public List<ProductResponse> getProductsByName(String name) {
        log.info("Fetching products with name containing: {}", name);

        List<ProductResponse> ans = new ArrayList<>();
        productRepository.findActiveByNameContaining(name).forEach(product ->
                ans.add(ProductMapper.mapProductToProductResponse(product, new ProductResponse())));
        return ans;
    }

    public ProductResponse createProduct(ProductCreateRequest product) {
        log.info("Creating new product: {}", product.getName());
        
        if (productRepository.existsByNameAndDeletedAtIsNull(product.getName())) {
            throw new IllegalArgumentException("Product with name '" + product.getName() + "' already exists");
        }

        var productToSave = ProductMapper.mapProductCreateRequestToProduct(product, new Product());
        
        Product savedProduct = productRepository.save(productToSave);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return ProductMapper.mapProductToProductResponse(savedProduct, new ProductResponse());
    }

    public ProductResponse updateProduct(Integer id, ProductUpdateRequest productDetails) {
        log.info("Updating product with id: {}", id);
        
        Product existingProduct = productRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        if (!existingProduct.getName().equals(productDetails.getName()) && 
            productRepository.existsByNameAndDeletedAtIsNull(productDetails.getName())) {
            throw new IllegalArgumentException("Product with name '" + productDetails.getName() + "' already exists");
        }

        ProductMapper.mapProductUpdateRequestToProduct(productDetails, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with id: {}", updatedProduct.getId());
        return ProductMapper.mapProductToProductResponse(updatedProduct, new ProductResponse());
    }

    public void deleteProduct(Integer id) {
        log.info("Soft deleting product with id: {}", id);
        
        Product product = productRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        product.setDeletedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        productRepository.save(product);
        log.info("Product soft deleted successfully with id: {}", id);
    }

    public boolean productExists(Integer id) {
        return productRepository.findActiveById(id).isPresent();
    }
}
