package com.service.marketplace.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SetProviderRequest {

    @NotEmpty
    private String role;
}
