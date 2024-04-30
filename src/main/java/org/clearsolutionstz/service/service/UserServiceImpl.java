package org.clearsolutionstz.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clearsolutionstz.data.entity.User;
import org.clearsolutionstz.data.repository.UserRepository;
import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserNotFoundException;
import org.clearsolutionstz.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public List<UserDto> listAll() {
        log.info("Getting all users from repository");
        return userMapper.toUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto add(UserDto note) {
        log.info("Adding user: {}", note);
        User entity = userMapper.toUserEntity(note);
        entity.setId(null);
        return userMapper.toUserDto(userRepository.save(userMapper.toUserEntity(note)));
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
    public void update(UserDto note) throws UserNotFoundException {
        log.info("Updating user: {}", note);
        if (!userRepository.existsById(note.getId())) {
            throw new UserNotFoundException("Not found user Id: " + note.getId());
        }
        userRepository.save(userMapper.toUserEntity(note));
    }

    @Override
    public UserDto getById(UUID id) throws UserNotFoundException {
        log.info("Getting user with id: {}", id);
        Optional<User> opNote = userRepository.findById(id);
        return userMapper.toUserDto(opNote.orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id))));
    }
}
