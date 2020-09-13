package ru.chupaYchups.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест что контроллер")
public class UsersControllerTest {

    public static final String TEST_USER_NAME = "Иван";
    public static final String TEST_USER_LOGIN = "testUser";
    public static final String TEST_USER_PASSWORD = "111";
    public static final int TEST_USER_ID = 0;
    public static final String USER_CREATE_PATH = "/user/create";
    public static final String USER_CREATE_VIEW = "userCreate.html";
    public static final String USER_SAVE_PATH = "/user/save";
    public static final String NAME_PARAM = "name";
    public static final String LOGIN_PARAM = "login";
    public static final String PASSWORD_PARAM = "password";
    public static final String ROOT_PATH = "/";
    public static final String USER_LIST_VIEW = "userList.html";

    private MockMvc mvc;
    private User testUser;

    @Mock
    private DBServiceUser dbServiceUser;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new UsersController(dbServiceUser)).build();
        testUser = new User(TEST_USER_ID, TEST_USER_NAME, TEST_USER_LOGIN, TEST_USER_PASSWORD);
    }

    @Test
    @DisplayName("корректно открывает страницу создания пользования при клике по ссылке перехода")
    void testThatCorrectlyRedirectToTheCreateUserPageByLink() throws Exception {
        mvc.perform(
                get(USER_CREATE_PATH)).
                andExpect(status().isOk()).
                andExpect(view().name(USER_CREATE_VIEW));
    }

    @Test
    @DisplayName("корректно обрабатывает запрос создания пользователя")
    public void testThatCorrectlyProcessCreateUserRequest() throws Exception {
        mvc.perform(
            post(USER_SAVE_PATH).
                param(NAME_PARAM, testUser.getName()).
                param(LOGIN_PARAM, testUser.getLogin()).
                param(PASSWORD_PARAM, testUser.getPassword())).
            andExpect(status().isFound()).
            andExpect(redirectedUrl(ROOT_PATH));
        verify(dbServiceUser, only()).saveUser(testUser);
    }

    @Test
    @DisplayName("корректно открывает страницу со списком пользователя")
    void testThatCorrectlyOpenUserListPage() throws Exception {
        given(dbServiceUser.findAllUsers()).willReturn(Optional.of(Arrays.asList(testUser)));
        mvc.perform(
            get(ROOT_PATH)).
            andExpect(status().isOk()).
            andExpect(view().name(USER_LIST_VIEW));
        verify(dbServiceUser, only()).findAllUsers();
    }
}