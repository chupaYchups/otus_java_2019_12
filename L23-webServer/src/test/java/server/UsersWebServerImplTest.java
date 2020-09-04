package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.web.server.UsersWebServer;
import ru.chupaYchups.web.server.UsersWebServerWithFilterBasedSecurity;
import ru.chupaYchups.web.services.TemplateProcessor;
import ru.chupaYchups.web.services.UserAuthService;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static server.utils.HttpUrlConnectionHelper.*;
import static server.utils.WebServerHelper.COOKIE_HEADER;
import static server.utils.WebServerHelper.login;

@DisplayName("Тест сервера должен ")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsersWebServerImplTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";

    private static final String LOGIN_URL = "login";
    private static final String API_USER_URL = "api/user";
    private static final String USERS_URL = "users";

    private static final String INCORRECT_USER_LOGIN = "BadUser";
    private static final long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_LOGIN = "user1";
    private static final String DEFAULT_USER_PASSWORD = "11111";
    public static final String DEFAULT_USER_NAME = "Vasya";
    private static final User DEFAULT_USER = new User(DEFAULT_USER_ID, DEFAULT_USER_NAME, DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);

    private static Gson gson;
    private static UsersWebServer webServer;

    public static final String TEST_USER_NAME = "testUserName";
    public static final String TEST_USER_LOGIN = "testUserLogin";
    public static final String TEST_PASSWORD = "testPassword";

    public static final String USERS_TEMPLATE = "users.html";

    private static DBServiceUser dbServiceUser;
    private static TemplateProcessor templateProcessor;

    @BeforeAll
    static void setUp() throws Exception {
        templateProcessor = mock(TemplateProcessor.class);
        dbServiceUser = mock(DBServiceUser.class);
        UserAuthService userAuthService = mock(UserAuthService.class);

        given(userAuthService.authenticate(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(true);
        given(userAuthService.authenticate(INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(false);
        given(dbServiceUser.getUser(DEFAULT_USER_ID)).willReturn(Optional.of(DEFAULT_USER));

        gson = new GsonBuilder().serializeNulls().create();
        webServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, userAuthService, dbServiceUser, gson, templateProcessor);
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("возвращать ID сессии при выполнении входа с верными данными")
    @Test
    void shouldReturnJSessionIdWhenLoggingInWithCorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();
    }

    @DisplayName("возвращать 302 при запросе пользователя по id если не выполнен вход ")
    @Test
    void shouldReturnForbiddenStatusForUserRequestWhenUnauthorized() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
    }

    @DisplayName("не возвращать ID сессии при выполнении входа если данные входа не верны")
    @Test
    void shouldNotReturnJSessionIdWhenLoggingInWithIncorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNull();
    }

    @DisplayName("возвращать корректные данные при запросе пользователя по id если вход выполнен")
    @Test
    void shouldReturnCorrectUserWhenAuthorized() throws IOException {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);

        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, List.of(String.valueOf(DEFAULT_USER_ID))), HttpMethod.GET);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        int responseCode = connection.getResponseCode();
        String response = readResponseFromConnection(connection);

        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response).isEqualTo(gson.toJson(DEFAULT_USER));
    }

    @DisplayName("Добавляет пользователя и возвращает cписок с корректным набором пользователей, с наличием добавленного")
    @Test
    void shouldCorrectCreateUserAndRefreshList() throws IOException {
        Mockito.reset(dbServiceUser, templateProcessor);
        //логин
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        //редирект на страницу с пользователями
        verify(dbServiceUser, times(1)).findAllUsers();
        verify(templateProcessor, atLeastOnce()).getPage(eq(USERS_TEMPLATE), eq(new HashMap<>()));

        Mockito.reset(dbServiceUser, templateProcessor);
        given(dbServiceUser.findAllUsers()).willReturn(Optional.of(Arrays.asList(DEFAULT_USER)));

        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, null), HttpMethod.POST);
        connection.setDoOutput(true);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        String bodyParams = String.format("userName=%s&userLogin=%s&userPassword=%s", DEFAULT_USER_NAME, DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        connection.getOutputStream().write(bodyParams.getBytes("UTF-8"));

        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_OK);

        verify(dbServiceUser, times(1)).
            saveUser(argThat(user ->
                user.getName().equals(DEFAULT_USER_NAME) &&
                user.getLogin().equals(DEFAULT_USER_LOGIN) &&
                user.getPassword().equals(DEFAULT_USER_PASSWORD)));

        //проверяем редирект
        verify(dbServiceUser, times(1)).findAllUsers();
        verify(templateProcessor, times(1)).
            getPage(eq(USERS_TEMPLATE), argThat(argument ->
                argument.containsKey("users") &&
                List.class.isInstance(argument.get("users")) &&
                ((List)argument.get("users")).size() == 1 &&
                ((List)argument.get("users")).get(0).equals(DEFAULT_USER)));
    }
}