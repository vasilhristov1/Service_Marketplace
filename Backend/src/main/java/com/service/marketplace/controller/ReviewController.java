package com.service.marketplace.controller;

import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable("reviewId") Integer reviewId) {
        ReviewResponse review = reviewService.getReviewById(reviewId);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable("userId") Integer userId) {
        List<ReviewResponse> userReviews = reviewService.getReviewByUserId(userId);
        return ResponseEntity.ok(userReviews);
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByServiceId(@PathVariable("serviceId") Integer serviceId) {
        List<ReviewResponse> serviceReviews = reviewService.getReviewByServiceId(serviceId);
        return ResponseEntity.ok(serviceReviews);
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> createReview(@ModelAttribute ReviewRequest reviewToCreate, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        ReviewResponse newReview = reviewService.createReview(reviewToCreate, files);
        return ResponseEntity.ok(newReview);
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable("reviewId") Integer reviewID, @RequestBody ReviewRequest reviewToUpdate) {
        ReviewResponse updatedReview = reviewService.updateReview(reviewID, reviewToUpdate);
        if (updatedReview != null) {
            return ResponseEntity.ok(updatedReview);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable("reviewId") Integer reviewId) {
        reviewService.deleteReviewById(reviewId);
        return ResponseEntity.ok().build();
    }
}
