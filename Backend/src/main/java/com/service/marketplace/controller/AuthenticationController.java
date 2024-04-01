package com.service.marketplace.controller;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.service.marketplace.dto.request.AuthenticationRequest;
import com.service.marketplace.dto.request.RegisterRequest;
import com.service.marketplace.dto.response.AuthenticationResponse;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.service.AuthenticationService;
import com.service.marketplace.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authService;
    private final GoogleService googleService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse authenticationResponse = authService.login(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/google/login")
    public ResponseEntity<AuthenticationResponse> googleLogin(
            @RequestBody AuthenticationResponse authenticationResponse
    ) throws GeneralSecurityException, IOException {
        AuthenticationResponse userResponse = googleService.verifyGoogleToken(authenticationResponse.getToken());
        if (userResponse != null) {
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAuthToken(@RequestBody UserResponse updatedUserData) {
        try {
            String refreshedToken = authService.refreshAuthToken(updatedUserData);
            return ResponseEntity.ok(refreshedToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User not found");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error refreshing token");
        }
    }
}
