package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.SetProviderRequest;
import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.mapper.UserMapper;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.CloudinaryService;
import com.service.marketplace.service.JwtService;
import com.service.marketplace.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Data
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            String email = user.getEmail();
            return userRepository.findByEmail(email).orElse(null);
        }

        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllByIsActive(true);
        return users.stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(userMapper::userToUserResponse)
                .orElse(null);
    }

    @Override
    public UserResponse updateUser(Integer userId, UserUpdateRequest userToUpdate, MultipartFile multipartFile) {
        User existingUser = null;

        if (userId != null) {
            existingUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        } else {
            existingUser = this.getCurrentUser();
        }

        if (multipartFile != null) {
            String newPictureUrl = null;

            existingUser = this.getCurrentUser();

            if (existingUser == null) {
                throw new EntityNotFoundException("User not found");
            }

            if (multipartFile != null) {
                try {
                    newPictureUrl = cloudinaryService.uploadFile(multipartFile);
                } catch (IOException e) {
                    String errorMessage = "Error uploading picture";
                    throw new RuntimeException(errorMessage, e);
                }
            }

            existingUser.setPicture(newPictureUrl);
        }

        String phoneNum = "";
        if (userToUpdate.getPhoneNumber() != null) {
            phoneNum = userToUpdate.getPhoneNumber();
        }
        existingUser.setFirstName(userToUpdate.getFirstName());
        existingUser.setLastName(userToUpdate.getLastName());
        existingUser.setPhoneNumber(phoneNum);
        existingUser.setDescription(userToUpdate.getDescription());

        userRepository.save(existingUser);

        return userMapper.userToUserResponse(existingUser);
    }

    @Override
    public UserResponse updateUserRole(Integer userId, SetProviderRequest providerRequest) {
        User existingUser = userRepository.findById(userId).orElse(null);

        String roleName = providerRequest.getRole();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        if (!existingUser.getRoles().contains(role)) {
            existingUser.getRoles().add(role);
            User updatedUser = userRepository.save(existingUser);
            return userMapper.userToUserResponse(updatedUser);
        }

        return userMapper.userToUserResponse(existingUser);
    }

    @Override
    public UserResponse updateUserRoleToProvider(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);

        Role role = roleRepository.findByName("PROVIDER").orElseThrow(() -> new IllegalArgumentException("Role not found."));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            User updatedUser = userRepository.save(user);
            return userMapper.userToUserResponse(updatedUser);
        }

        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse getUserResponseByUser(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setRoles(user.getRoles());
        return userMapper.userToUserResponse(user);
    }

    @Override
    public boolean deleteUserById(Integer userId) {
        return userRepository.findById(userId).map(user -> {
            user.setActive(false);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Could not find any user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}
