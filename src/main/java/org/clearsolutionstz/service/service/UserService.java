package org.clearsolutionstz.service.service;

import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserAgeRestrictionException;
import org.clearsolutionstz.service.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> listAll();
    UserDto add(UserDto note) throws UserAgeRestrictionException;
    void deleteById(UUID id) throws UserNotFoundException;
    void update(UserDto note) throws UserNotFoundException, UserAgeRestrictionException;
    UserDto getById(UUID id) throws UserNotFoundException;
}
