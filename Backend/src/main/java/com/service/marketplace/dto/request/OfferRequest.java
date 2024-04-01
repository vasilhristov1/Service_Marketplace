package com.service.marketplace.dto.request;

import com.service.marketplace.persistence.enums.OfferStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OfferRequest {

    @NotNull(message = "Offer ID cannot be null")
    private Integer offerId;

    @NotNull(message = "Request ID cannot be null")
    private Integer request_id;

    @NotNull(message = "Provider ID cannot be null")
    private Integer provider_id;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;

    private OfferStatus offerStatus = OfferStatus.PENDING;
}
