package com.service.marketplace.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CityRequest {

    @NotEmpty
    @Size(min = 2, max = 50)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String zipCode;

    private String address;
}
