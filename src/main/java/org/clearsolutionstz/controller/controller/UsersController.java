package org.clearsolutionstz.controller.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clearsolutionstz.controller.response.UserResponse;
import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserAgeRestrictionException;
import org.clearsolutionstz.service.exception.UserNotFoundException;
import org.clearsolutionstz.service.mapper.UserMapper;
import org.clearsolutionstz.service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.clearsolutionstz.controller.request.CreateUserRequest;
import org.clearsolutionstz.controller.request.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/Users")
public class UsersController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> userList() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userMapper.toUserResponses(userService.listAll()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> userSearch() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userMapper.toUserResponses(userService.listAll()));
    }

    @GetMapping("/edit")
    public ResponseEntity<UserResponse> getUserById(@RequestParam("id") UUID id) throws UserNotFoundException {
        UserDto userDto = userService.getById(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userMapper.toUserResponse(userDto));
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(
        @RequestBody @Valid @NotNull UpdateUserRequest request) throws UserNotFoundException, UserAgeRestrictionException {
        userService.update(userMapper.toUserDto(request.getId(), request));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @NotNull @RequestBody CreateUserRequest request) throws UserAgeRestrictionException {
        UserDto newNote = userService.add(userMapper.toUserDto(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toUserResponse(newNote));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@RequestBody UpdateUserRequest request) throws UserNotFoundException {
        userService.deleteById(request.getId());
    }
}
