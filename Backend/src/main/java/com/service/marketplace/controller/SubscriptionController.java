package com.service.marketplace.controller;

import com.service.marketplace.dto.response.SubscriptionResponse;
import com.service.marketplace.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        List<SubscriptionResponse> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable("subscriptionId") Integer subscriptionId) {
        SubscriptionResponse subscription = subscriptionService.getSubscriptionById(subscriptionId);
        if (subscription != null) {
            return ResponseEntity.ok(subscription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionByUserId(@PathVariable("userId") Integer userId) {
        SubscriptionResponse userSubscription = subscriptionService.getSubscriptionByUserId(userId);
        return ResponseEntity.ok(userSubscription);
    }

    @DeleteMapping("/delete/{subscriptionId}")
    public ResponseEntity<Void> deleteService(@PathVariable("subscriptionId") Integer subscriptionId) {
        subscriptionService.deleteSubscriptionById(subscriptionId);
        return ResponseEntity.ok().build();
    }
}
