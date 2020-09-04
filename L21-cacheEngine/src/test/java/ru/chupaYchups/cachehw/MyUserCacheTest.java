package ru.chupaYchups.cachehw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chupaYchups.core.model.User;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест кеша пользователей должен проверить")
class MyUserCacheTest {

    private HwCache<String, User> cache;

    @Mock
    private Map<String, User> cacheMap;

    @Mock
    private List<HwListener<String, User>> listenerList;

    @BeforeEach
    void setUp() {
        cache = new MyCache<>(cacheMap, listenerList);
    }

    @Test
    @DisplayName("что значение корректно кладётся в map хранящий кеш")
    void shouldСorrectPutValueInCacheMap() {
        User user = new User(1L, "userName");
        String cacheId = Long.toString(user.getId());

        cache.put(cacheId, user);

        Mockito.verify(cacheMap, Mockito.only()).put(cacheId, user);
    }

    @Test
    @DisplayName("что значение корректно удаляется из map кеша")
    void shouldСorrectRemoveValueFromCacheMap() {
        String keyString = Long.toString(1L);

        cache.remove(keyString);

        Mockito.verify(cacheMap, Mockito.only()).remove(keyString);
    }

    @Test
    @DisplayName("что корректно получаем значение из map кеша")
    void shouldСorrectGetValueFromCacheMap() {
        User user = new User(1l, "testUser");
        String testUserId = Long.toString(user.getId());

        Mockito.when(cacheMap.get(testUserId)).thenReturn(user);
        User returnedUser = cache.get(testUserId);

        Mockito.verify(cacheMap, Mockito.only()).get(testUserId);

        assertThat(returnedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("что корректно добавляем слушателя в список слушателей")
    void shouldСorrectAddCacheListener() {
        HwListener<String, User> listener = (key, value, action) -> {
        };

        cache.addListener(listener);

        Mockito.verify(listenerList, Mockito.only()).add(listener);
    }

    @Test
    @DisplayName("что корректно удаляем слушателя из списка слушателей")
    void shouldСorrectremoveCacheListener() {
        HwListener<String, User> listener = (key, value, action) -> {
        };

        cache.removeListener(listener);

        Mockito.verify(listenerList, Mockito.only()).remove(listener);
    }
}