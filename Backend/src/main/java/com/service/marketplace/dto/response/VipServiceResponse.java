package com.service.marketplace.dto.response;

import lombok.Data;

@Data
public class VipServiceResponse {
    private Integer serviceId;
    private String startDate;
    private String endDate;
    private boolean isActive;
    private String stripeId;
}
