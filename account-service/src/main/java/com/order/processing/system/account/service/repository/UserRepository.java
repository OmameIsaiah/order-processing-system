package com.order.processing.system.account.service.repository;


import com.order.processing.system.account.service.enums.UserType;
import com.order.processing.system.account.service.model.Users;
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
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query(nativeQuery = false, value = "SELECT u FROM Users u WHERE u.uuid=:uuid")
    Optional<Users> findUserByUUID(@Param("uuid") String uuid);

    @Query(nativeQuery = false, value = "SELECT u FROM Users u WHERE u.email=:email")
    Optional<Users> findUserByEmail(@Param("email") String email);

    @Query(nativeQuery = false, value = "SELECT u FROM Users u WHERE u.email LIKE %:keyword% OR u.name  LIKE %:keyword%  OR u.userType LIKE %:keyword%")
    List<Users> searchUsers(@Param("keyword") String keyword);

    @Query(nativeQuery = false, value = "SELECT u FROM Users u WHERE u.userType=:userType")
    Page<Users> findUsersByType(@Param("userType") UserType userType, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Users u WHERE u.uuid=:uuid")
    void deleteUserByUUID(@Param("uuid") String uuid);
}
