package com.service.marketplace.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty
    @Size(min = 2, max = 20)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String lastName;

    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    private String email;

    @NotEmpty
    @Size(min = 5)
    private String password;
    private String confirmPassword;
}

