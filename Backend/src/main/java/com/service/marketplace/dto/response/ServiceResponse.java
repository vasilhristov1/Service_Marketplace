package com.service.marketplace.dto.response;

import com.service.marketplace.persistence.enums.ServiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceResponse {
    private Integer id;
    private String title;
    private String description;
    private ServiceStatus serviceStatus;
    private BigDecimal price;
    private Integer providerId;
    private Integer categoryId;
    private List<Integer> cityIds;
    private LocalDateTime updatedAt;
    private boolean isVip;
}
