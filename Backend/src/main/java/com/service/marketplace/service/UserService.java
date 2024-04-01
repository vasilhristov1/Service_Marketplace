package com.service.marketplace.service;

import com.service.marketplace.dto.request.SetProviderRequest;
import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.persistence.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User getCurrentUser();

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Integer userId);

    UserResponse updateUser(Integer userId, UserUpdateRequest userToUpdate, MultipartFile multipartFile);

    boolean deleteUserById(Integer userId);

    UserResponse updateUserRole(Integer userId, SetProviderRequest providerRequest);

    UserResponse getUserResponseByUser(User user);

    UserResponse updateUserRoleToProvider(Integer userId);

    void updateResetPasswordToken(String token, String email);

    User getByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);
}
