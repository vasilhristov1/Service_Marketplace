package com.service.marketplace.service;

import com.service.marketplace.dto.response.AuthenticationResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleService {
    AuthenticationResponse verifyGoogleToken(String googleToken) throws IOException, GeneralSecurityException;
}
