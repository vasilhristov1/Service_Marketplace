package com.service.marketplace.dto.response;

import com.service.marketplace.persistence.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer experience;
    private Double rating;
    private String description;
    private String picture;
    private Set<Role> roles;
    private String stripeAccountId;
}
