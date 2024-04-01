package com.service.marketplace.service;

public interface EmailSenderService {
    void sendSimpleEmail(String toEmail, String subject, String body);
}
