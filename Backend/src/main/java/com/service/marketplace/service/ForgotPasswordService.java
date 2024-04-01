package com.service.marketplace.service;

import com.service.marketplace.dto.request.ForgetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface ForgotPasswordService {
    void processForgotPassword(String email);
    void processResetPassword(ForgetPasswordRequest request);
}
