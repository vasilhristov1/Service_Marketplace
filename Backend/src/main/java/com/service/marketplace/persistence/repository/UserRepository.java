package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findAllByIsActive(boolean isActive);

    User findByResetPasswordToken(String token);
}

