package com.emir201.dualshare.mapper;

import com.emir201.dualshare.data.local.entity.UserEntity;
import com.emir201.dualshare.data.remote.dto.UserResponseDTO;

public class UserMapper {
    private UserMapper() {
    }

    public static UserEntity toEntity(UserResponseDTO dto) {
        UserEntity entity = new UserEntity();

        entity.setId(dto.id());
        entity.setUsername(dto.username());
        entity.setEmail(dto.email());
        entity.setShareCode(dto.shareCode());
        entity.setPhotoUrl(dto.photoUrl());
        entity.setRole(dto.role());
        entity.setEnabled(dto.enabled());

        return entity;
    }

    public static UserResponseDTO toDTO(UserEntity entity) {
        UserResponseDTO dto = new UserResponseDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getShareCode(),
                entity.getPhotoUrl(),
                entity.getRole(),
                entity.isEnabled()
        );

        return dto;
    }
}
