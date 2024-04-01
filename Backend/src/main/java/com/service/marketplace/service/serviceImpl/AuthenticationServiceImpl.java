package com.service.marketplace.service.serviceImpl;


import com.service.marketplace.dto.request.AuthenticationRequest;
import com.service.marketplace.dto.request.RegisterRequest;
import com.service.marketplace.dto.response.AuthenticationResponse;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.AuthenticationService;
import com.service.marketplace.service.EmailSenderService;
import com.service.marketplace.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;

    public AuthenticationResponse register(RegisterRequest request) {
        Role role = roleRepository.findByName("CUSTOMER").orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .isActive(true)
                .build();
        userRepository.save(user);

        String emailSubject = "Thank You for Registering!";
        String emailBody = String.format("Dear %s %s,\n" +
                "\n" +
                "Congratulations! Your registration with our platform is now complete. \uD83C\uDF89\n" +
                "\n" +
                "Thank you for choosing us! We are thrilled to welcome you to our community and are excited about the journey ahead. You now have access to a wide range of features and opportunities.\n" +
                "\n" +
                "Should you have any questions or require assistance, please do not hesitate to contact us. We are here to support you!\n" +
                "\n" +
                "Best regards,\n" +
                "\n" +
                "Our Platform Team", user.getFirstName(), user.getLastName());

        emailSenderService.sendSimpleEmail(user.getEmail(), emailSubject, emailBody);

        return null;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public String refreshAuthToken(UserResponse updatedUserData) {
        User user = userRepository.findByEmail(updatedUserData.getEmail()).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Generate a new JWT token with updated user information
        return jwtService.generateToken(user);
    }
}