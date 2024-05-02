package org.clearsolutionstz;


import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserDataRestrictionException;
import org.clearsolutionstz.service.exception.UserNotFoundException;
import org.clearsolutionstz.service.mapper.UserMapper;
import org.clearsolutionstz.service.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@Sql(statements = "delete from users where 1;", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    private UserDto user;

    @BeforeEach
    public void beforeEach() {
        user = new UserDto();
        user.setFirstName("Vik");
        user.setLastName("Tor");
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setEmail("e@mail.com");
        user.setPhone("111-222-3333");
        user.setAddress("Address 33333");
    }

    @Test
    void testAddUser() throws UserDataRestrictionException {
        //When
        UserDto userObj = userService.add(user);

        //Then
        Assertions.assertNotNull(userObj.getId());
    }

    @Test
    void testGetById() throws UserNotFoundException, UserDataRestrictionException {
        //When
        user.setFirstName("TEST");
        UserDto userAdded = userService.add(user);
        UserDto userObj = userService.getById(userMapper.toUserEntity(userAdded).getId());

        //Then
        String  expected = "TEST";
        Assertions.assertEquals(expected, userObj.getFirstName());
    }

    @Test
    void testUpdate() throws UserNotFoundException, UserDataRestrictionException {
        //When
        UserDto userAdded = userService.add(user);
        userAdded.setFirstName("UPDATED");
        userService.update(userAdded);
        UserDto userObj = userService.getById(userAdded.getId());

        //Then
        String expected = "UPDATED";
        Assertions.assertEquals(expected, userObj.getFirstName());
    }

    @Test
    void testDelete() throws UserNotFoundException, UserDataRestrictionException {
        //When
        UserDto userAdded = userService.add(user);
        List<UserDto> listUsers = userService.listAll();
        int expected = 1;
        Assertions.assertEquals(expected, listUsers.size());

        userService.deleteById(userAdded.getId());
        listUsers = userService.listAll();

        //Then
        expected = 0;
        Assertions.assertEquals(expected, listUsers.size());
    }

    @Test
    void testListAll() throws UserDataRestrictionException {
        //When
        userService.add(user);
        List<UserDto> listUsers = userService.listAll();

        //Then
        int expected = 1;
        Assertions.assertEquals(expected, listUsers.size());
    }

    @Test
    void testDeleteUserNotFoundException() {
        //When-Then
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteById(id));
    }

    @Test
    void testUpdateUserNotFoundException() {
        //When-Then
        user.setId(UUID.randomUUID());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(user));
    }

    @Test
    void testGetByIdUserNotFoundException() {
        //When-Then
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getById(id));
    }

    @Test
    void testAddUserAgeRestrictionException() {
        //When-Then
        user.setBirthDate(LocalDate.now());
        Assertions.assertThrows(UserDataRestrictionException.class, () -> userService.add(user));
    }

    @Test
    void testUpdateUserAgeRestrictionException() throws UserDataRestrictionException {
        //When-Then
        UserDto userDto = userService.add(user);
        userDto.setBirthDate(LocalDate.now());
        Assertions.assertThrows(UserDataRestrictionException.class, () -> userService.update(userDto));
    }

    @Test
    void testSearchStartBDateEndBDateException() {
        //When-Then
        user.setBirthDate(LocalDate.now());
        Assertions.assertThrows(UserDataRestrictionException.class, () -> userService.getByBirthDateBetween(LocalDate.now(), LocalDate.now().minusDays(1)));
    }

}
