package org.clearsolutionstz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.clearsolutionstz.controller.controller.UsersController;
import org.clearsolutionstz.controller.controller.RootController;
import org.clearsolutionstz.service.dto.UserDto;
import org.clearsolutionstz.service.exception.UserNotFoundException;
import org.clearsolutionstz.service.mapper.UserMapper;
import org.clearsolutionstz.service.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@Sql(statements = "delete from note;", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ClearSolutionsTzApplicationTests {

    @Autowired
    private RootController rootController;
    @Autowired
    private UsersController usersController;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    private UserDto user;

    @BeforeEach
    public void beforeEach() {
        user = new UserDto();
        user.setTitle("Title");
        user.setContent("Content");
    }

    @Test
    void contextLoads() {
        assertThat(rootController).isNotNull();
        assertThat(usersController).isNotNull();
    }

    @Test
    void testAddNote() {
        //When
        UserDto noteObj = userService.add(user);

        //Then
        Assertions.assertNotNull(noteObj.getId());
    }

    @Test
    void testGetById() throws UserNotFoundException {
        //When
        user.setTitle("TEST");
        UserDto noteAdded = userService.add(user);
        UserDto noteObj = userService.getById(userMapper.toNoteEntity(noteAdded).getId());

        //Then
        String  expected = "TEST";
        Assertions.assertEquals(expected, noteObj.getTitle());
    }

    @Test
    void testUpdate() throws UserNotFoundException {
        //When
        UserDto noteAdded = userService.add(user);
        noteAdded.setTitle("UPDATED");
        userService.update(noteAdded);
        UserDto noteObj = userService.getById(noteAdded.getId());

        //Then
        String expected = "UPDATED";
        Assertions.assertEquals(expected, noteObj.getTitle());
    }

    @Test
    void testDelete() throws UserNotFoundException {
        //When
        UserDto noteAdded = userService.add(user);
        List<UserDto> listNotes = userService.listAll();
        int expected = 1;
        Assertions.assertEquals(expected, listNotes.size());

        userService.deleteById(noteAdded.getId());
        listNotes = userService.listAll();

        //Then
        expected = 0;
        Assertions.assertEquals(expected, listNotes.size());
    }

    @Test
    void testListAll() {
        //When
        userService.add(user);
        List<UserDto> listNotes = userService.listAll();

        //Then
        int expected = 1;
        Assertions.assertEquals(expected, listNotes.size());
    }

    @Test
    void testDeleteNoteNotFoundException() {
        //When-Then
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteById(null));
    }

    @Test
    void testUpdateNoteNotFoundException() {
        //When-Then
        user.setId(UUID.randomUUID());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(user));
    }

    @Test
    void testGetByIdNoteNotFoundException() {
        //When-Then
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getById(id));
    }


}
