package com.service.marketplace.dto.request;

import lombok.Data;

@Data
public class StripeAccountRequest {
    private String email;
    private String firstMiddleName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private String iban;
}
