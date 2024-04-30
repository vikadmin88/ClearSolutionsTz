package org.clearsolutionstz.service.mapper;

import org.clearsolutionstz.controller.response.UserResponse;
import org.clearsolutionstz.data.entity.User;
import org.clearsolutionstz.service.dto.UserDto;
import org.springframework.stereotype.Component;
import org.clearsolutionstz.controller.request.CreateUserRequest;
import org.clearsolutionstz.controller.request.UpdateUserRequest;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public List<User> toUserEntities(Collection<UserDto> dtos) {
        return dtos.stream()
                .map(this::toUserEntity)
                .collect(Collectors.toList());
    }

    public User toUserEntity(UserDto dto) {
        User entity = User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
        return entity;
    }

    public List<UserDto> toUserDtos(Collection<User> entities) {
        return entities.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto toUserDto(User entity) {
        UserDto dto = UserDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDate())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
        return dto;
    }

    public List<UserResponse> toUserResponses(Collection<UserDto> dtos) {
        return dtos.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse toUserResponse(UserDto dto) {
        UserResponse response = UserResponse.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
        return response;
    }

    public List<UserDto> requestsToUserDtos(Collection<CreateUserRequest> requests) {
        return requests.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto toUserDto(CreateUserRequest request) {
        UserDto dto = UserDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        return dto;
    }

    public UserDto toUserDto(UUID id, UpdateUserRequest request) {
        UserDto dto = UserDto.builder()
                .id(id)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        return dto;
    }
}