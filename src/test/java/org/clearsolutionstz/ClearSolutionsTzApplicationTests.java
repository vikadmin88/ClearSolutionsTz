package org.clearsolutionstz;

import org.clearsolutionstz.controller.controller.RootController;
import org.clearsolutionstz.controller.controller.UsersController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ClearSolutionsTzApplicationTests {

    @Autowired
    private RootController rootController;
    @Autowired
    private UsersController usersController;

    @Test
    void contextLoads() {
        assertThat(rootController).isNotNull();
        assertThat(usersController).isNotNull();
    }
}
