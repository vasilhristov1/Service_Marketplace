package com.service.marketplace.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceFilterRequest {
    private BigDecimal minPrice = BigDecimal.valueOf(0.0);
    private BigDecimal maxPrice = BigDecimal.valueOf(1000.0);
    private List<Integer> categoryIds = new ArrayList<>();
    private List<Integer> cityIds = new ArrayList<>();
    private Integer page = 0;
    private Integer pageSize = 5;
    private String sortingField = "updatedAt";
    private String sortingDirection = "DESC";
}
