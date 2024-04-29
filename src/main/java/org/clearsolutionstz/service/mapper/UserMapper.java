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

    public List<User> toNoteEntities(Collection<UserDto> dtos) {
        return dtos.stream()
                .map(this::toNoteEntity)
                .collect(Collectors.toList());
    }

    public User toNoteEntity(UserDto dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        return entity;
    }

    public List<UserDto> toNoteDtos(Collection<User> entities) {
        return entities.stream()
                .map(this::toNoteDto)
                .collect(Collectors.toList());
    }

    public UserDto toNoteDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        return dto;
    }

    public List<UserResponse> toNoteResponses(Collection<UserDto> dtos) {
        return dtos.stream()
                .map(this::toNoteResponse)
                .collect(Collectors.toList());
    }

    public UserResponse toNoteResponse(UserDto dto) {
        UserResponse response = new UserResponse();
        response.setId(dto.getId());
        response.setTitle(dto.getTitle());
        response.setContent(dto.getContent());
        return response;
    }

    public List<UserDto> requestsToNoteDtos(Collection<CreateUserRequest> requests) {
        return requests.stream()
                .map(this::toNoteDto)
                .collect(Collectors.toList());
    }

    public UserDto toNoteDto(CreateUserRequest request) {
        UserDto dto = new UserDto();
        dto.setTitle(request.getTitle());
        dto.setContent(request.getContent());
        return dto;
    }

    public UserDto toNoteDto(UUID id, UpdateUserRequest request) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setTitle(request.getTitle());
        dto.setContent(request.getContent());
        return dto;
    }
}