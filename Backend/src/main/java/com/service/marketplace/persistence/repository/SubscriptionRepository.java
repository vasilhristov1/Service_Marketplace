package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Subscription;
import com.service.marketplace.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findByUser(User user);
    Subscription findByStripeId(String stripeId);
    List<Subscription> findByIsActiveTrue();
}
