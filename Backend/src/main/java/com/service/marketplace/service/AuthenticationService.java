package com.service.marketplace.service;

import com.service.marketplace.dto.request.AuthenticationRequest;
import com.service.marketplace.dto.request.RegisterRequest;
import com.service.marketplace.dto.response.AuthenticationResponse;
import com.service.marketplace.dto.response.UserResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(AuthenticationRequest request);

    String refreshAuthToken(UserResponse updatedUserData);
}

