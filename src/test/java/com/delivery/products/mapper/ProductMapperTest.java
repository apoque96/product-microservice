package com.delivery.products.mapper;

import com.delivery.products.dto.ProductCreateRequest;
import com.delivery.products.dto.ProductResponse;
import com.delivery.products.dto.ProductUpdateRequest;
import com.delivery.products.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void mapProductToProductResponse_ShouldMapAllFields_WhenValidInput() {
        // Arrange
        Product product = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("29.99"))
                .description("Test Description")
                .createdAt(LocalDateTime.of(2023, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2023, 1, 2, 10, 0))
                .deletedAt(null)
                .build();

        ProductResponse productResponse = new ProductResponse();

        // Act
        ProductResponse result = ProductMapper.mapProductToProductResponse(product, productResponse);

        // Assert
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getCreatedAt(), result.getCreatedAt());
        assertEquals(product.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void mapProductToProductResponse_ShouldHandleNullFields_WhenProductHasNullValues() {
        // Arrange
        Product product = Product.builder()
                .id(null)
                .name(null)
                .price(null)
                .description(null)
                .createdAt(null)
                .updatedAt(null)
                .deletedAt(null)
                .build();

        ProductResponse productResponse = new ProductResponse();

        // Act
        ProductResponse result = ProductMapper.mapProductToProductResponse(product, productResponse);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getPrice());
        assertNull(result.getDescription());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    void mapProductCreateRequestToProduct_ShouldMapAllFields_WhenValidInput() {
        // Arrange
        ProductCreateRequest createRequest = new ProductCreateRequest();
        createRequest.setName("New Product");
        createRequest.setPrice(new BigDecimal("19.99"));
        createRequest.setDescription("New Description");

        Product product = new Product();

        // Act
        Product result = ProductMapper.mapProductCreateRequestToProduct(createRequest, product);

        // Assert
        assertNotNull(result);
        assertEquals(createRequest.getName(), result.getName());
        assertEquals(createRequest.getPrice(), result.getPrice());
        assertEquals(createRequest.getDescription(), result.getDescription());
    }

    @Test
    void mapProductCreateRequestToProduct_ShouldHandleNullFields_WhenRequestHasNullValues() {
        // Arrange
        ProductCreateRequest createRequest = new ProductCreateRequest();
        createRequest.setName(null);
        createRequest.setPrice(null);
        createRequest.setDescription(null);

        Product product = new Product();

        // Act
        Product result = ProductMapper.mapProductCreateRequestToProduct(createRequest, product);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getPrice());
        assertNull(result.getDescription());
    }

    @Test
    void mapProductUpdateRequestToProduct_ShouldMapAllFields_WhenValidInput() {
        // Arrange
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setPrice(new BigDecimal("39.99"));
        updateRequest.setDescription("Updated Description");

        Product product = new Product();

        // Act
        Product result = ProductMapper.mapProductUpdateRequestToProduct(updateRequest, product);

        // Assert
        assertNotNull(result);
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getPrice(), result.getPrice());
        assertEquals(updateRequest.getDescription(), result.getDescription());
    }

    @Test
    void mapProductUpdateRequestToProduct_ShouldHandleNullFields_WhenRequestHasNullValues() {
        // Arrange
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setName(null);
        updateRequest.setPrice(null);
        updateRequest.setDescription(null);

        Product product = new Product();

        // Act
        Product result = ProductMapper.mapProductUpdateRequestToProduct(updateRequest, product);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getPrice());
        assertNull(result.getDescription());
    }

    @Test
    void mapProductToProductResponse_ShouldReturnSameResponseInstance_WhenMapping() {
        // Arrange
        Product product = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("29.99"))
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductResponse productResponse = new ProductResponse();

        // Act
        ProductResponse result = ProductMapper.mapProductToProductResponse(product, productResponse);

        // Assert
        assertSame(productResponse, result);
    }

    @Test
    void mapProductCreateRequestToProduct_ShouldReturnSameProductInstance_WhenMapping() {
        // Arrange
        ProductCreateRequest createRequest = new ProductCreateRequest();
        createRequest.setName("Test Product");
        createRequest.setPrice(new BigDecimal("29.99"));
        createRequest.setDescription("Test Description");

        Product product = new Product();

        // Act
        Product result = ProductMapper.mapProductCreateRequestToProduct(createRequest, product);

        // Assert
        assertSame(product, result);
    }
}
