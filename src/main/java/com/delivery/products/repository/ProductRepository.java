package com.delivery.products.repository;

import com.delivery.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL")
    List<Product> findAllActive();

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Product> findActiveById(@Param("id") Integer id);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.deletedAt IS NULL")
    List<Product> findActiveByNameContaining(@Param("name") String name);

    boolean existsByNameAndDeletedAtIsNull(String name);
}
