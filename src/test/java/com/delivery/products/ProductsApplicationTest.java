package com.delivery.products;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080"
})
class ProductsApplicationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        assertDoesNotThrow(() -> {});
    }
}
