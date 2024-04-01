package com.service.marketplace.controller;

import com.service.marketplace.dto.request.ForgetPasswordRequest;
import com.service.marketplace.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/process")
    public ResponseEntity<String> processForgotPassword(@RequestParam String email) {
        try {
            forgotPasswordService.processForgotPassword(email);
            return ResponseEntity.ok("Password reset link sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send password reset link: " + e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> processResetPassword(@RequestBody ForgetPasswordRequest request) {
        try {
            forgotPasswordService.processResetPassword(request);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reset password: " + e.getMessage());
        }
    }
}
