package com.service.marketplace.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Integer id;
    private String description;
    private double rating;
    private Integer customerId;
    private Integer serviceId;
    private boolean isActive;
    private LocalDateTime updatedAt;
}
