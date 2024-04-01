package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.dto.response.UserUpdateResponse;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userUpdateRequestToUser(UserUpdateRequest request);

    UserUpdateResponse userToUserUpdateResponse(User user);

    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);

    UserResponse userToUserResponse(User user);

    User userRoleToUser(UserRole role);
}
