package core.demo;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.cachehw.HwCache;
import ru.chupaYchups.cachehw.HwListener;
import ru.chupaYchups.cachehw.MyCache;
import ru.chupaYchups.core.hibernate.HibernateUtils;
import ru.chupaYchups.core.hibernate.dao.UserDaoHibernate;
import ru.chupaYchups.core.hibernate.sessionmanager.SessionManagerHibernate;
import ru.chupaYchups.core.model.Address;
import ru.chupaYchups.core.model.Phone;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.core.service.DbServiceUserImpl;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверяем что класс сервиса")
public class DbServiceUserIntegrationTest {

    public static final String TEST_USER_NAME = "testUserName";
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserIntegrationTest.class);

    private DBServiceUser dbServiceUser;
    private SessionFactory sessionFactory;
    private Map<String, User> cacheMap;
    private HwCache<String, User> cache;
    private SessionManagerHibernate sessionManagerHibernate;

    public static final int ZERO_ID = 0;
    public static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";
    public static final String TEST_ADDRESS_NEW_VASIUKI = "Новые Васюки";
    public static final String TEST_PHONE_01 = "01";
    public static final String TEST_PHONE_02 = "02";

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML, Phone.class, Address.class, User.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        UserDaoHibernate userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);

        cacheMap = new WeakHashMap<>();
        cache = new MyCache<>(cacheMap, new ArrayList<>());
        cache.addListener(new HwListener<String, User>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info(action + " : " + key + " - " + value);
            }
        });

        dbServiceUser = new DbServiceUserImpl(userDaoHibernate, cache);
    }

    @Test
    @DisplayName("корректно находит пользователя по идентификатору")
    void shouldCorrectFindUserById() {
        User user = new User(ZERO_ID, "testUserName");
        Phone phone01 = new Phone(user, TEST_PHONE_01);
        Phone phone02 = new Phone(user, TEST_PHONE_02);
        Address address = new Address(user, TEST_ADDRESS_NEW_VASIUKI);
        user.setAddress(address);
        user.setPhones(Arrays.asList(phone01, phone02));
        saveUser(user);
        assertThat(user.getId()).isGreaterThan(ZERO_ID);

        Optional<User> foundUser = dbServiceUser.getUser(user.getId());

        assertThat(foundUser).isPresent().get().isEqualTo(user);
        assertThat(foundUser.get().getAddress()).isNotNull().isEqualTo(user.getAddress());
        assertThat(foundUser.get().getPhones()).isNotEmpty().hasSize(2).hasSameElementsAs(user.getPhones());
    }

    @Test
    @DisplayName("корректно сохраняет пользователя в БД")
    void shouldCorrectSaveUser() {
        User user = new User(ZERO_ID, "testUserName");
        Phone phone01 = new Phone(user, TEST_PHONE_01);
        Phone phone02 = new Phone(user, TEST_PHONE_02);
        Address address = new Address(user, TEST_ADDRESS_NEW_VASIUKI);
        user.setAddress(address);
        user.setPhones(Arrays.asList(phone01, phone02));

        long savedId = dbServiceUser.saveUser(user);
        assertThat(savedId).isGreaterThan(ZERO_ID);

        User savedUser = getUser(savedId);
        assertThat(savedUser).isEqualTo(user);
        assertThat(savedUser.getPhones()).isNotEmpty().hasSize(2);
        assertThat(savedUser.getPhones().get(0)).isEqualTo(user.getPhones().get(0));
        assertThat(savedUser.getPhones().get(1)).isEqualTo(user.getPhones().get(1));
        assertThat(savedUser.getAddress()).isEqualTo(user.getAddress());
    }

    private void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    private User getUser(long userId) {
        User user;
        try (Session session = sessionFactory.openSession()) {
            user = session.find(User.class, userId);
            Hibernate.initialize(user.getPhones());
            Hibernate.initialize(user.getAddress());
        }
        return user;
    }


    @Test
    @DisplayName("корректно очищает кеш, в случае запуска GC")
    void shouldCorrectClearCacheWhenTheGCRunning() {
        final int TEST_USER_QTY = 10;
        System.gc();
        IntStream.range(1, TEST_USER_QTY + 1).forEach(value -> {
            User user = new User(value, "testUserName" + value);
            dbServiceUser.saveUser(user);
        });
        assertThat(cacheMap.keySet()).isNotEmpty();
        System.gc();
        assertThat(cacheMap.keySet()).isEmpty();
    }

    @Test
    @DisplayName("с кешем работает быстрее чем без кеша")
    void shouldWorkFasterWithCache() {
        //Добавили пользователей в БД
        final int TEST_USER_QTY = 100;
        IntStream.range(1, TEST_USER_QTY).forEach(value -> {
            User user = new User(0, TEST_USER_NAME + value);
            saveUser(user);
        });
        System.gc();
        //Пробуем достать их из базы, без кеша
        long startTime = System.nanoTime();
        IntStream.range(1, TEST_USER_QTY).forEach(value -> {
            dbServiceUser.getUser(value);
        });
        long withoutCacheDuration = System.nanoTime() - startTime;
        //пробуем достать их из кеша, ведь они уже там
        startTime = System.nanoTime();
        IntStream.range(1, TEST_USER_QTY).forEach(value -> {
            dbServiceUser.getUser(value);
        });
        long withCacheDuration = System.nanoTime() - startTime;

        logger.info("withoutCacheDuration : " + withoutCacheDuration + ", withCacheDuration : " + withCacheDuration);
        assertThat(withoutCacheDuration).isGreaterThan(withCacheDuration);
    }
}
