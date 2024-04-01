package com.service.marketplace.dto.request;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
    private String token;
    private String password;
}