package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.ForgetPasswordRequest;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.service.EmailSenderService;
import com.service.marketplace.service.ForgotPasswordService;
import com.service.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    public void processForgotPassword(String email) {
        String token = generateRandomString();

        userService.updateResetPasswordToken(token, email);
        String resetPasswordLink = "http://localhost:3000" + "/reset-password?token=" + token;
        String subject = "Link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        emailSenderService.sendSimpleEmail(email, subject, content);
    }

    public void processResetPassword(ForgetPasswordRequest request) {
        String token = request.getToken();
        String password = request.getPassword();

        User user = userService.getByResetPasswordToken(token);

        if (user == null) {
            throw new IllegalStateException("Entity not found");
        } else {
            userService.updatePassword(user, password);
        }
    }

    private static String generateRandomString() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int LENGTH = 45;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}