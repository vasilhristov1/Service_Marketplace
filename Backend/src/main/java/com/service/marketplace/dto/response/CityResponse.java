package com.service.marketplace.dto.response;

import lombok.Data;

@Data
public class CityResponse {
    private Integer id;
    private String name;
    private String zipCode;
    private String address;
}
