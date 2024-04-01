package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.FilesRequest;
import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.mapper.ReviewMapper;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.ReviewRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.FilesService;
import com.service.marketplace.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final FilesService filesService;

    @Override
    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        return reviewMapper.toReviewResponseList(reviews);
    }

    @Override
    public ReviewResponse getReviewById(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review != null) {
            return reviewMapper.reviewToReviewResponse(review);
        } else {
            return null;
        }
    }

    @Override
    public ReviewResponse createReview(ReviewRequest reviewToCreate, MultipartFile[] files) {
        User customer = userRepository.findById(reviewToCreate.getCustomerId()).orElse(null);
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(reviewToCreate.getServiceId()).orElse(null);

        Review newReview = reviewMapper.reviewRequestToReview(reviewToCreate, customer, service);
        ReviewResponse reviewResponse = reviewMapper.reviewToReviewResponse(reviewRepository.save(newReview));

        if (files != null) {
            for (MultipartFile multipartFile : files) {
                FilesRequest filesRequest = new FilesRequest(multipartFile, null, reviewResponse.getId());
                filesService.createFile(filesRequest);
            }
        }

        return reviewResponse;
    }

    @Override
    public ReviewResponse updateReview(Integer reviewId, ReviewRequest reviewToUpdate) {
        Review existingReview = reviewRepository.findById(reviewId).orElse(null);
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(reviewToUpdate.getServiceId()).orElse(null);

        Review updatedReview = reviewMapper.reviewRequestToReview(reviewToUpdate, service);

        if (existingReview != null) {
            existingReview.setDescription(updatedReview.getDescription());
            existingReview.setRating(updatedReview.getRating());
            existingReview.setUpdatedAt(updatedReview.getUpdatedAt());

            return reviewMapper.reviewToReviewResponse(reviewRepository.save(existingReview));
        } else {
            return null;
        }
    }

    @Override
    public void deleteReviewById(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewResponse> getReviewByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            List<Review> userReviews = reviewRepository.findByCustomer(user);

            return reviewMapper.toReviewResponseList(userReviews);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReviewResponse> getReviewByServiceId(Integer serviceId) {
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(serviceId).orElse(null);

        if (service != null) {
            List<Review> serviceReviews = reviewRepository.findByService(service);

            return reviewMapper.toReviewResponseList(serviceReviews);
        } else {
            return Collections.emptyList();
        }
    }
}
