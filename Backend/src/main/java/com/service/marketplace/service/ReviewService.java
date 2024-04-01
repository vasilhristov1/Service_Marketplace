package com.service.marketplace.service;

import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getAllReviews();

    ReviewResponse getReviewById(Integer reviewId);

    ReviewResponse createReview(ReviewRequest reviewToCreate, MultipartFile[] files);

    ReviewResponse updateReview(Integer reviewId, ReviewRequest reviewToUpdate);

    void deleteReviewById(Integer reviewId);

    List<ReviewResponse> getReviewByUserId(Integer userId);

    List<ReviewResponse> getReviewByServiceId(Integer serviceId);
}
