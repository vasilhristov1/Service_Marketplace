package com.service.marketplace.service;

import com.service.marketplace.dto.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionResponse> getAllSubscriptions();

    SubscriptionResponse getSubscriptionById(Integer subscriptionId);

    void deleteSubscriptionById(Integer subscriptionId);

    SubscriptionResponse getSubscriptionByUserId(Integer userId);
}
