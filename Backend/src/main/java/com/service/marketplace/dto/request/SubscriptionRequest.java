package com.service.marketplace.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionRequest {
    private Integer userId;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private String stripeId;
}
