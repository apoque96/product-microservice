package com.delivery.products.service;

import com.delivery.products.dto.ProductCreateRequest;
import com.delivery.products.dto.ProductResponse;
import com.delivery.products.dto.ProductUpdateRequest;
import com.delivery.products.entity.Product;
import com.delivery.products.mapper.ProductMapper;
import com.delivery.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductCreateRequest createRequest;
    private ProductUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("29.99"))
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deletedAt(null)
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
    void getAllProducts_ShouldReturnAllActiveProducts() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAllActive()).thenReturn(products);

        // Act
        List<ProductResponse> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        assertEquals(testProduct.getPrice(), result.get(0).getPrice());
        verify(productRepository).findAllActive();
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList_WhenNoActiveProducts() {
        // Arrange
        when(productRepository.findAllActive()).thenReturn(Arrays.asList());

        // Act
        List<ProductResponse> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAllActive();
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        // Arrange
        when(productRepository.findActiveById(1)).thenReturn(Optional.of(testProduct));

        // Act
        ProductResponse result = productService.getProductById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct.getId(), result.getId());
        assertEquals(testProduct.getName(), result.getName());
        assertEquals(testProduct.getPrice(), result.getPrice());
        verify(productRepository).findActiveById(1);
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findActiveById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById(1);
        });
        assertEquals("No product found with id: 1", exception.getMessage());
        verify(productRepository).findActiveById(1);
    }

    @Test
    void getProductsByName_ShouldReturnProducts_WhenProductsMatchName() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findActiveByNameContaining("Test")).thenReturn(products);

        // Act
        List<ProductResponse> result = productService.getProductsByName("Test");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        verify(productRepository).findActiveByNameContaining("Test");
    }

    @Test
    void getProductsByName_ShouldReturnEmptyList_WhenNoProductsMatchName() {
        // Arrange
        when(productRepository.findActiveByNameContaining("NonExistent")).thenReturn(Arrays.asList());

        // Act
        List<ProductResponse> result = productService.getProductsByName("NonExistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findActiveByNameContaining("NonExistent");
    }

    @Test
    void createProduct_ShouldCreateProduct_WhenValidRequest() {
        // Arrange
        Product productToSave = Product.builder()
                .name(createRequest.getName())
                .price(createRequest.getPrice())
                .description(createRequest.getDescription())
                .build();

        Product savedProduct = Product.builder()
                .id(1)
                .name(createRequest.getName())
                .price(createRequest.getPrice())
                .description(createRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productRepository.existsByNameAndDeletedAtIsNull(createRequest.getName())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ProductResponse result = productService.createProduct(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(savedProduct.getId(), result.getId());
        assertEquals(savedProduct.getName(), result.getName());
        assertEquals(savedProduct.getPrice(), result.getPrice());
        verify(productRepository).existsByNameAndDeletedAtIsNull(createRequest.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrowException_WhenProductWithNameExists() {
        // Arrange
        when(productRepository.existsByNameAndDeletedAtIsNull(createRequest.getName())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(createRequest);
        });
        assertEquals("Product with name '" + createRequest.getName() + "' already exists", exception.getMessage());
        verify(productRepository).existsByNameAndDeletedAtIsNull(createRequest.getName());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenProductExistsAndNameIsUnique() {
        // Arrange
        Product existingProduct = Product.builder()
                .id(1)
                .name("Old Name")
                .price(new BigDecimal("10.00"))
                .description("Old Description")
                .build();

        when(productRepository.findActiveById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByNameAndDeletedAtIsNull(updateRequest.getName())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        ProductResponse result = productService.updateProduct(1, updateRequest);

        // Assert
        assertNotNull(result);
        verify(productRepository).findActiveById(1);
        verify(productRepository).existsByNameAndDeletedAtIsNull(updateRequest.getName());
        verify(productRepository).save(existingProduct);
        assertEquals(updateRequest.getName(), existingProduct.getName());
        assertEquals(updateRequest.getPrice(), existingProduct.getPrice());
        assertEquals(updateRequest.getDescription(), existingProduct.getDescription());
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenSameName() {
        // Arrange
        updateRequest.setName(testProduct.getName());
        when(productRepository.findActiveById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        ProductResponse result = productService.updateProduct(1, updateRequest);

        // Assert
        assertNotNull(result);
        verify(productRepository).findActiveById(1);
        verify(productRepository, never()).existsByNameAndDeletedAtIsNull(anyString());
        verify(productRepository).save(testProduct);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findActiveById(1)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1, updateRequest);
        });
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository).findActiveById(1);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrowException_WhenNameAlreadyExists() {
        // Arrange
        Product existingProduct = Product.builder()
                .id(1)
                .name("Old Name")
                .build();

        when(productRepository.findActiveById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByNameAndDeletedAtIsNull(updateRequest.getName())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1, updateRequest);
        });
        assertEquals("Product with name '" + updateRequest.getName() + "' already exists", exception.getMessage());
        verify(productRepository).findActiveById(1);
        verify(productRepository).existsByNameAndDeletedAtIsNull(updateRequest.getName());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldSoftDeleteProduct_WhenProductExists() {
        // Arrange
        when(productRepository.findActiveById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        productService.deleteProduct(1);

        // Assert
        verify(productRepository).findActiveById(1);
        verify(productRepository).save(testProduct);
        assertNotNull(testProduct.getDeletedAt());
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findActiveById(1)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(1);
        });
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository).findActiveById(1);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void productExists_ShouldReturnTrue_WhenProductExists() {
        // Arrange
        when(productRepository.findActiveById(1)).thenReturn(Optional.of(testProduct));

        // Act
        boolean result = productService.productExists(1);

        // Assert
        assertTrue(result);
        verify(productRepository).findActiveById(1);
    }

    @Test
    void productExists_ShouldReturnFalse_WhenProductNotExists() {
        // Arrange
        when(productRepository.findActiveById(1)).thenReturn(Optional.empty());

        // Act
        boolean result = productService.productExists(1);

        // Assert
        assertFalse(result);
        verify(productRepository).findActiveById(1);
    }
}
