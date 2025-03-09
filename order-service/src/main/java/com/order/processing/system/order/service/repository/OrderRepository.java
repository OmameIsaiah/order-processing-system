package com.order.processing.system.order.service.repository;


import com.order.processing.system.order.service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(nativeQuery = false, value = "SELECT o FROM Order o WHERE o.uuid=:uuid")
    Optional<Order> findOrderByUuid(@Param("uuid") String uuid);

    @Query(nativeQuery = false, value = "SELECT o FROM Order o")
    Page<Order> findAllOrders(Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "UPDATE Order o SET o.status='CANCEL' WHERE o.uuid=:uuid")
    void cancelOrder(@Param("uuid") String uuid);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM Order o WHERE o.uuid=:uuid")
    void deleteOrderByUuid(@Param("uuid") String uuid);
}
