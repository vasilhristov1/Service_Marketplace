package com.service.marketplace.dto.response;

import com.service.marketplace.persistence.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer experience;
    private String description;
    private String pictureUrl;
    private Double rating;
    private Set<Role> roles;

}
