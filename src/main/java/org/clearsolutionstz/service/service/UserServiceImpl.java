package org.clearsolutionstz.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clearsolutionstz.data.entity.User;
import org.clearsolutionstz.data.repository.UserRepository;
import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserAgeRestrictionException;
import org.clearsolutionstz.service.exception.UserNotFoundException;
import org.clearsolutionstz.service.mapper.UserMapper;
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

    @Override
    public List<UserDto> listAll() {
        log.info("Getting all users from repository");
        return userMapper.toUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto add(UserDto user) throws UserAgeRestrictionException {
        log.info("Adding user: {}", user);
        if (isUserAgeNotValid(user)) {
            throw new UserAgeRestrictionException("User age less than required");
        }
        User entity = userMapper.toUserEntity(user);
        entity.setId(null);
        return userMapper.toUserDto(userRepository.save(userMapper.toUserEntity(user)));
    }

    @Override
    public void update(UserDto user) throws UserNotFoundException, UserAgeRestrictionException {
        log.info("Updating user: {}", user);
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("Not found user Id: " + user.getId());
        }
        if (isUserAgeNotValid(user)) {
            throw new UserAgeRestrictionException("User age less than required");
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

    private boolean isUserAgeNotValid(UserDto user) {
        int ageRestrict = Integer.parseInt(Objects.requireNonNull(env.getProperty("user.settings.age")));
        int userAge = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        log.info("Checking user age. min age {} User age: {}", ageRestrict, userAge);
        return userAge < ageRestrict;
    }

}
