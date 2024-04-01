package com.service.marketplace.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewRequest {

    @NotEmpty
    @Size(min = 2, max = 200)
    private String description;

    @NotEmpty
    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private double rating;

    @NotEmpty
    private Integer customerId;

    @NotEmpty
    private Integer serviceId;

    private boolean isActive = true;
}
