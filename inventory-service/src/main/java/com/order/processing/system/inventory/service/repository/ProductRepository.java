package com.order.processing.system.inventory.service.repository;


import com.order.processing.system.inventory.service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(nativeQuery = false, value = "SELECT p FROM Product p WHERE p.uuid=:uuid")
    Optional<Product> findProductByUuid(@Param("uuid") String uuid);

    @Query(nativeQuery = false, value = "SELECT p FROM Product p WHERE p.name=:name")
    Optional<Product> findProductByName(@Param("name") String name);

    @Query(nativeQuery = false, value = "SELECT p FROM Product p WHERE p.name LIKE %:name% ")
    List<Product> searchProductByName(@Param("name") String name);

    @Query(nativeQuery = false, value = "SELECT p FROM Product p")
    Page<Product> findAllProducts(Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM Product p WHERE p.uuid=:uuid")
    void deleteProductByUuid(@Param("uuid") String uuid);
}
