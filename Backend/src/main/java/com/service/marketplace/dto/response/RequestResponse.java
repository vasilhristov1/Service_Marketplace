package com.service.marketplace.dto.response;

import com.service.marketplace.persistence.enums.OfferStatus;
import lombok.Data;

@Data
public class RequestResponse {
    private Integer id;
    private Integer customerId;
    private Integer serviceId;
    private String description;
    private OfferStatus requestStatus;
}
