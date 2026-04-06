package com.delivery.products.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JpaAuditingConfigTest {

    @Test
    void auditorProvider_ShouldReturnSystemUser() {
        // Arrange
        JpaAuditingConfig config = new JpaAuditingConfig();
        AuditorAware<String> auditorProvider = config.auditorProvider();

        // Act
        Optional<String> currentAuditor = auditorProvider.getCurrentAuditor();

        // Assert
        assertNotNull(auditorProvider);
        assertTrue(currentAuditor.isPresent());
        assertEquals("system", currentAuditor.get());
    }

    @Test
    void auditorProvider_ShouldAlwaysReturnSystemUser() {
        // Arrange
        JpaAuditingConfig config = new JpaAuditingConfig();
        AuditorAware<String> auditorProvider = config.auditorProvider();

        // Act - call multiple times
        Optional<String> currentAuditor1 = auditorProvider.getCurrentAuditor();
        Optional<String> currentAuditor2 = auditorProvider.getCurrentAuditor();

        // Assert
        assertTrue(currentAuditor1.isPresent());
        assertTrue(currentAuditor2.isPresent());
        assertEquals("system", currentAuditor1.get());
        assertEquals("system", currentAuditor2.get());
    }

    @Test
    void configClass_ShouldHaveCorrectAnnotations() {
        // Assert that the configuration class has the correct annotations
        assertTrue(JpaAuditingConfig.class.isAnnotationPresent(Configuration.class));
        assertTrue(JpaAuditingConfig.class.isAnnotationPresent(EnableJpaAuditing.class));
        assertTrue(JpaAuditingConfig.class.isAnnotationPresent(Profile.class));
        
        Profile profileAnnotation = JpaAuditingConfig.class.getAnnotation(Profile.class);
        assertArrayEquals(new String[]{"!test"}, profileAnnotation.value());
    }
}
