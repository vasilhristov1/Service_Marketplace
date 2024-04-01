package com.service.marketplace.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferPaymentRequest {

    private String description;
    private BigDecimal price;
    private String priceId;
    private String successUrl;
    private String cancelUrl;
    private String email;
    private String userId;
    private Integer offerId;
}
