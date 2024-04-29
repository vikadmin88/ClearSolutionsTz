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
        log.info("Getting all notes from repository");
        return userMapper.toNoteDtos(userRepository.findAll());
    }

    @Override
    public UserDto add(UserDto note) {
        log.info("Adding note: {}", note);
        User entity = userMapper.toNoteEntity(note);
        entity.setId(null);
        return userMapper.toNoteDto(userRepository.save(userMapper.toNoteEntity(note)));
    }

    @Override
    public void deleteById(UUID id) throws UserNotFoundException {
        log.info("Deleting note with id: {}", id);
        if (Objects.isNull(id)) {
            throw new UserNotFoundException("Not found note Id");
        }
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Not found note Id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void update(UserDto note) throws UserNotFoundException {
        log.info("Updating note: {}", note);
        if (!userRepository.existsById(note.getId())) {
            throw new UserNotFoundException("Not found note Id: " + note.getId());
        }
        userRepository.save(userMapper.toNoteEntity(note));
    }

    @Override
    public UserDto getById(UUID id) throws UserNotFoundException {
        log.info("Getting note with id: {}", id);
        Optional<User> opNote = userRepository.findById(id);
        return userMapper.toNoteDto(opNote.orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id))));
    }
}
