package com.authx.mapper;

import com.authx.entity.UserEntity;
import com.authx.userdto.AdminResponse;

public class UserMapper {

    public static AdminResponse toDTO(UserEntity user) {
        return AdminResponse.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .email(user.getEmail())
            .isAccountVerified(user.getIsAccountVerified())
            .build();
    }
}
