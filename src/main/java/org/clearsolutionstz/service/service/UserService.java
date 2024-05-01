package org.clearsolutionstz.service.service;

import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserDataRestrictionException;
import org.clearsolutionstz.service.exception.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> listAll();
    UserDto add(UserDto note) throws UserDataRestrictionException;
    void deleteById(UUID id) throws UserNotFoundException;
    void update(UserDto note) throws UserNotFoundException, UserDataRestrictionException;
    UserDto getById(UUID id) throws UserNotFoundException;
    List<UserDto> getByBirthDateBetween(LocalDate startDate, LocalDate endDate) throws UserDataRestrictionException;
}
