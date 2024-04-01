package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    List<ReviewResponse> toReviewResponseList(List<Review> reviews);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "rating", source = "request.rating")
    @Mapping(target = "active", source = "request.active")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Review reviewRequestToReview(ReviewRequest request, User customer, Service service);

    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "rating", source = "request.rating")
    @Mapping(target = "active", source = "request.active")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Review reviewRequestToReview(ReviewRequest request, Service service);

    @Mapping(target = "customerId", source = "review.customer.id")
    @Mapping(target = "serviceId", source = "review.service.id")
    ReviewResponse reviewToReviewResponse(Review review);
}
