package com.delivery.products.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CtTest {

    private final Ct ct = new Ct();

    @Test
    void d_ShouldReturnDivisionResult_WhenValidInput() {
        // Act & Assert
        assertEquals(5, ct.d(10, 2));
        assertEquals(0, ct.d(0, 5));
        assertEquals(-3, ct.d(-9, 3));
        assertEquals(2, ct.d(7, 3)); // Integer division
    }

    @Test
    void d_ShouldReturnMaxValue_WhenDivisorIsZero() {
        // Act & Assert
        assertEquals(Integer.MAX_VALUE, ct.d(10, 0));
        assertEquals(Integer.MAX_VALUE, ct.d(0, 0));
        assertEquals(Integer.MAX_VALUE, ct.d(-5, 0));
    }

    @Test
    void d_ShouldHandleNegativeNumbers() {
        // Act & Assert
        assertEquals(3, ct.d(-9, -3));
        assertEquals(-3, ct.d(9, -3));
        assertEquals(-2, ct.d(-7, 3));
    }

    @Test
    void d_ShouldHandleEdgeCases() {
        // Act & Assert
        assertEquals(1, ct.d(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, ct.d(Integer.MAX_VALUE, 0));
        assertEquals(0, ct.d(1, Integer.MAX_VALUE));
    }
}
