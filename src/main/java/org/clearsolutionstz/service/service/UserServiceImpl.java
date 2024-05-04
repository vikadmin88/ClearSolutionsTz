package org.clearsolutionstz.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clearsolutionstz.data.entity.User;
import org.clearsolutionstz.data.repository.UserRepository;
import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserDataRestrictionException;
import org.clearsolutionstz.service.exception.UserNotFoundException;
import org.clearsolutionstz.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Environment env;
    @Value("${user.settings.restrictedAge}")
    private int userRestrictedAge;

    @Override
    public List<UserDto> listAll() {
        log.info("Getting all users from repository");
        return userMapper.toUserDtos(userRepository.findAll());
    }

    @Override
    public List<UserDto> getByBirthDateBetween(LocalDate startDate, LocalDate endDate) throws UserDataRestrictionException {
        log.info("Getting users by birthdate between {} and {}", startDate, endDate);
        if (endDate.isBefore(startDate)) {
            throw new UserDataRestrictionException("From date of birth must be before To date of birth");
        }
        return userMapper.toUserDtos(userRepository.findByBirthDateBetween(startDate, endDate));
    }

    @Override
    public UserDto add(UserDto user) throws UserDataRestrictionException {
        log.info("Adding user: {}", user);
        if (!isUserAgeValid(user)) {
            throw new UserDataRestrictionException("Required minimum user age:: " + userRestrictedAge);
        }
        User entity = userMapper.toUserEntity(user);
        entity.setId(null);
        return userMapper.toUserDto(userRepository.save(userMapper.toUserEntity(user)));
    }

    @Override
    public void update(UserDto user) throws UserNotFoundException, UserDataRestrictionException {
        log.info("Updating user: {}", user);
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("Not found user Id: " + user.getId());
        }
        if (!isUserAgeValid(user)) {
            throw new UserDataRestrictionException("Required minimum user age:: " + userRestrictedAge);
        }
        userRepository.save(userMapper.toUserEntity(user));
    }

    @Override
    public void deleteById(UUID id) throws UserNotFoundException {
        log.info("Deleting user with id: {}", id);
        if (Objects.isNull(id)) {
            throw new UserNotFoundException("Not found user Id");
        }
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Not found user Id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getById(UUID id) throws UserNotFoundException {
        log.info("Getting user with id: {}", id);
        Optional<User> opUser = userRepository.findById(id);
        return userMapper.toUserDto(opUser.orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id))));
    }

    private boolean isUserAgeValid(UserDto user) {
        int userAge = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        log.info("Checking user age. min age {} User age: {}", userRestrictedAge, userAge);
        return userAge >= userRestrictedAge;
    }

}
