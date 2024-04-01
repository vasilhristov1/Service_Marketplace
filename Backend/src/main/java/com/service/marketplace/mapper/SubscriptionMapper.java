package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.SubscriptionRequest;
import com.service.marketplace.dto.response.SubscriptionResponse;
import com.service.marketplace.persistence.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    Subscription subscriptionRequestToSubscription(SubscriptionRequest request);

    @Mapping(target = "userId", source = "subscription.user.id")
    SubscriptionResponse subscriptionToSubscriptionResponse(Subscription subscription);

    List<SubscriptionResponse> toSubscriptionResponseList(List<Subscription> subscriptions);
}
