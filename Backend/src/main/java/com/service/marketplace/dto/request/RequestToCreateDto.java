package com.service.marketplace.dto.request;

import com.service.marketplace.persistence.enums.OfferStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestToCreateDto {

    @NotNull(message = "Customer ID cannot be null")
    private Integer customerId;

    @NotNull(message = "Service ID cannot be null")
    private Integer serviceId;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    private OfferStatus requestStatus = OfferStatus.PENDING;
}
