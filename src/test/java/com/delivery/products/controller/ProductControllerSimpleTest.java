package com.delivery.products.controller;

import com.delivery.products.dto.ProductCreateRequest;
import com.delivery.products.dto.ProductResponse;
import com.delivery.products.dto.ProductUpdateRequest;
import com.delivery.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerSimpleTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponse testProductResponse;
    private ProductCreateRequest createRequest;
    private ProductUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        testProductResponse = ProductResponse.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("29.99"))
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        createRequest = new ProductCreateRequest();
        createRequest.setName("New Product");
        createRequest.setPrice(new BigDecimal("19.99"));
        createRequest.setDescription("New Description");

        updateRequest = new ProductUpdateRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setPrice(new BigDecimal("39.99"));
        updateRequest.setDescription("Updated Description");
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testProductResponse.getName(), response.getBody().get(0).getName());
        verify(productService).getAllProducts();
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList_WhenNoProducts() {
        // Arrange
        when(productService.getAllProducts()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(productService).getAllProducts();
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        // Arrange
        when(productService.getProductById(1)).thenReturn(testProductResponse);

        // Act
        ResponseEntity<ProductResponse> response = productController.getProductById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testProductResponse.getId(), response.getBody().getId());
        assertEquals(testProductResponse.getName(), response.getBody().getName());
        verify(productService).getProductById(1);
    }

    @Test
    void getProductsByName_ShouldReturnProducts_WhenProductsMatchName() {
        // Arrange
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.getProductsByName("Test")).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponse>> response = productController.getProductsByName("Test");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testProductResponse.getName(), response.getBody().get(0).getName());
        verify(productService).getProductsByName("Test");
    }

    @Test
    void getProductsByName_ShouldReturnEmptyList_WhenNoProductsMatchName() {
        // Arrange
        when(productService.getProductsByName("NonExistent")).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<ProductResponse>> response = productController.getProductsByName("NonExistent");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(productService).getProductsByName("NonExistent");
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct_WhenValidRequest() {
        // Arrange
        when(productService.createProduct(any(ProductCreateRequest.class))).thenReturn(testProductResponse);

        // Act
        ResponseEntity<ProductResponse> response = productController.createProduct(createRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testProductResponse.getId(), response.getBody().getId());
        assertEquals(testProductResponse.getName(), response.getBody().getName());
        verify(productService).createProduct(any(ProductCreateRequest.class));
    }

    @Test
    void createProduct_ShouldReturnBadRequest_WhenInvalidRequest() {
        // Arrange
        when(productService.createProduct(any(ProductCreateRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid product data"));

        // Act
        ResponseEntity<ProductResponse> response = productController.createProduct(createRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).createProduct(any(ProductCreateRequest.class));
    }

    @Test
    void createProduct_ShouldReturnBadRequest_WhenProductAlreadyExists() {
        // Arrange
        when(productService.createProduct(any(ProductCreateRequest.class)))
                .thenThrow(new IllegalArgumentException("Product already exists"));

        // Act
        ResponseEntity<ProductResponse> response = productController.createProduct(createRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).createProduct(any(ProductCreateRequest.class));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenValidRequest() {
        // Arrange
        when(productService.updateProduct(eq(1), any(ProductUpdateRequest.class))).thenReturn(testProductResponse);

        // Act
        ResponseEntity<ProductResponse> response = productController.updateProduct(1, updateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testProductResponse.getId(), response.getBody().getId());
        assertEquals(testProductResponse.getName(), response.getBody().getName());
        verify(productService).updateProduct(eq(1), any(ProductUpdateRequest.class));
    }

    @Test
    void updateProduct_ShouldReturnNotFound_WhenProductNotExists() {
        // Arrange
        when(productService.updateProduct(eq(1), any(ProductUpdateRequest.class)))
                .thenThrow(new IllegalArgumentException("Product not found"));

        // Act
        ResponseEntity<ProductResponse> response = productController.updateProduct(1, updateRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).updateProduct(eq(1), any(ProductUpdateRequest.class));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent_WhenProductExists() {
        // Arrange
        doNothing().when(productService).deleteProduct(1);

        // Act
        ResponseEntity<Void> response = productController.deleteProduct(1);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).deleteProduct(1);
    }

    @Test
    void deleteProduct_ShouldReturnNotFound_WhenProductNotExists() {
        // Arrange
        doThrow(new IllegalArgumentException("Product not found")).when(productService).deleteProduct(1);

        // Act
        ResponseEntity<Void> response = productController.deleteProduct(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).deleteProduct(1);
    }

    @Test
    void productExists_ShouldReturnTrue_WhenProductExists() {
        // Arrange
        when(productService.productExists(1)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = productController.productExists(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        verify(productService).productExists(1);
    }

    @Test
    void productExists_ShouldReturnFalse_WhenProductNotExists() {
        // Arrange
        when(productService.productExists(1)).thenReturn(false);

        // Act
        ResponseEntity<Boolean> response = productController.productExists(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
        verify(productService).productExists(1);
    }
}
