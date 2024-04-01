package com.service.marketplace.service.serviceImpl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.service.marketplace.dto.response.AuthenticationResponse;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.EmailSenderService;
import com.service.marketplace.service.GoogleService;
import com.service.marketplace.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class GoogleServiceImpl implements GoogleService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;

    public AuthenticationResponse verifyGoogleToken(String googleToken) throws IOException, GeneralSecurityException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .build();
        GoogleIdToken idToken = verifier.verify(googleToken);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            try {
                User existingUser = userRepository.findByEmail(payload.getEmail()).orElseThrow();
                String jwtToken = jwtService.generateToken(existingUser);
                return new AuthenticationResponse(jwtToken);

            } catch (Exception e) {
                Role role = roleRepository.findByName("CUSTOMER").orElseThrow();
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                String pictureUrl = (String) payload.get("picture");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                User newUser = new User();
                newUser.setEmail(payload.getEmail());
                newUser.setFirstName(givenName);
                newUser.setLastName(familyName);
                newUser.setPicture(pictureUrl);
                newUser.setRoles(roles);
                userRepository.save(newUser);

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
                        "Our Platform Team", newUser.getFirstName(), newUser.getLastName());

                emailSenderService.sendSimpleEmail(newUser.getEmail(), emailSubject, emailBody);

                String jwtToken = jwtService.generateToken(newUser);
                return new AuthenticationResponse(jwtToken);
            }
        }
        return null;
    }
}



