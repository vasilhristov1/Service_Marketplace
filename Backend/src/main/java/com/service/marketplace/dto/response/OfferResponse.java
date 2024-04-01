package com.service.marketplace.dto.response;

import com.service.marketplace.persistence.enums.OfferStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferResponse {
    private Integer Id;
    private Integer requestId;
    private Integer providerId;
    private String description;
    private BigDecimal price;
    private OfferStatus offerStatus;
}
