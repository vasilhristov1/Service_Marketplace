package com.service.marketplace.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Checkout {
    private String priceId;
    private String successUrl;
    private String cancelUrl;
    private String email;
    private String userId;
    private String serviceId;
}
