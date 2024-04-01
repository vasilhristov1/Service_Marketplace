package com.service.marketplace.dto.request;

import com.service.marketplace.persistence.enums.ServiceStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ServiceRequest {

    @NotEmpty
    @Size(min = 2, max = 50)
    private String title;

    @NotEmpty
    @Size(min = 2, max = 200)
    private String description;

    private ServiceStatus serviceStatus;

    @NotEmpty
    private BigDecimal price;

    @NotEmpty
    private Integer providerId;

    @NotEmpty
    private Integer categoryId;

    private List<Integer> cityIds;
}
