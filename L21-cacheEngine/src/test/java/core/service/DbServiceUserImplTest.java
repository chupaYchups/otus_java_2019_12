package core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.chupaYchups.cachehw.HwCache;
import ru.chupaYchups.core.dao.UserDao;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DbServiceException;
import ru.chupaYchups.core.service.DbServiceUserImpl;
import ru.chupaYchups.core.sessionmanager.SessionManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для работы с пользователями в рамках БД должен ")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DbServiceUserImplTest {

    private static final long USER_ID = 1L;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private UserDao userDao;

    @Mock
    private HwCache<String, User> cache;

    private DbServiceUserImpl dbServiceUser;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        given(userDao.getSessionManager()).willReturn(sessionManager);
        inOrder = inOrder(userDao, sessionManager, cache);
        dbServiceUser = new DbServiceUserImpl(userDao, cache);
    }

    @Test
    @DisplayName(" корректно сохранять пользователя")
    void shouldCorrectSaveUser() {
        var vasya = new User();
        doAnswer(invocation ->{
            vasya.setId(USER_ID);
            return null;
        }).when(userDao).insertOrUpdate(vasya);

        long id = dbServiceUser.saveUser(vasya);
        assertThat(id).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName(" при сохранении пользователя, открывать и коммитить транзакцию в нужном порядке")
    void shouldCorrectSaveUserAndOpenAndCommitTranInExpectedOrder() {
        User user = new User(1L, "testUserName");

        dbServiceUser.saveUser(user);

        inOrder.verify(userDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).commitSession();
        inOrder.verify(cache, times(1)).put(Long.toString(user.getId()), user);
        inOrder.verify(sessionManager, never()).rollbackSession();
    }

    @Test
    @DisplayName(" при сохранении пользователя, открывать и откатывать транзакцию в нужном порядке")
    void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {
        doThrow(IllegalArgumentException.class).when(userDao).insertOrUpdate(any());

        assertThatThrownBy(() -> dbServiceUser.saveUser(null))
                .isInstanceOf(DbServiceException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

        inOrder.verify(userDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).rollbackSession();
        inOrder.verify(sessionManager, never()).commitSession();
        inOrder.verify(cache, never()).put(any(), any());
    }

    @Test
    @DisplayName(" корректно загружать закешированного пользователя по заданному id")
    void shouldLoadCorrectCachedUserById() {
        User expectedUser = new User(USER_ID, "Вася");
        final String expectedUserIdString = Long.toString(USER_ID);
        given(cache.get(expectedUserIdString)).willReturn(expectedUser);

        Optional<User> mayBeUser = dbServiceUser.getUser(USER_ID);

        inOrder.verify(cache, times(1)).get(any());
        inOrder.verify(userDao, never()).findById(anyLong());

        assertThat(mayBeUser).isPresent().get().isEqualToComparingFieldByField(expectedUser);
    }

    @Test
    @DisplayName(" корректно загружать незакешированного пользователя по заданному id")
    void shouldLoadCorrectNotCachedUserById() {
        User expectedUser = new User(USER_ID, "Вася");
        final String expectedUserIdString = Long.toString(USER_ID);
        given(cache.get(expectedUserIdString)).willReturn(null);
        given(userDao.findById(USER_ID)).willReturn(Optional.of(expectedUser));

        Optional<User> mayBeUser = dbServiceUser.getUser(USER_ID);

        inOrder.verify(cache, times(1)).get(expectedUserIdString);
        inOrder.verify(userDao, times(1)).findById(USER_ID);

        assertThat(mayBeUser).isPresent().get().isEqualToComparingFieldByField(expectedUser);
    }
}