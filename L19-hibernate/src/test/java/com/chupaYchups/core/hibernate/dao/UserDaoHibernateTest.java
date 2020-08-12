package com.chupaYchups.core.hibernate.dao;

import com.chupaYchups.core.hibernate.HibernateUtils;
import com.chupaYchups.core.hibernate.sessionmanager.SessionManagerHibernate;
import com.chupaYchups.core.model.Address;
import com.chupaYchups.core.model.Phone;
import com.chupaYchups.core.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

@DisplayName("Dao должен")
class UserDaoHibernateTest {

    public static final String TEST_USER_NAME = "testUser";
    public static final String TEST_USER_UPDATED_NAME = "testUserUpdated";
    public static final int ZERO_ID = 0;
    public static final String TEST_ADDRESS_MAYAK = "Маяковская";
    public static final String TEST_ADDRESS_NEW_VASUIKI = "Новые Васюки";
    public static final String TEST_PHONE_01 = "01";
    public static final String TEST_PHONE_02 = "02";
    public static final String TEST_PHONE_03 = "03";
    public static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";


    private UserDaoHibernate userDaoHibernate;
    private SessionManagerHibernate sessionManagerHibernate;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML, Phone.class, Address.class, User.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
    }

    @Test
    @DisplayName("находить пользователя по идентификатору")
    void shouldFindCorrectUserByIdentificator() {
        User testUser = new User(ZERO_ID, TEST_USER_NAME);
        Phone phone = new Phone(testUser, TEST_PHONE_01);
        Address address = new Address(testUser, TEST_ADDRESS_MAYAK);
        testUser.setPhone(Arrays.asList(phone));
        testUser.setAddress(address);

        sessionManagerHibernate.beginSession();
        saveUser(testUser);
        Optional<User> foundUserOptional = userDaoHibernate.findById(testUser.getId());
        assertThat(foundUserOptional).isPresent();
        User user = foundUserOptional.get();
        Hibernate.initialize(user.getPhone());
        Hibernate.initialize(user.getAddress());
        sessionManagerHibernate.commitSession();

        assertThat(user).isEqualTo(testUser);
        assertThat(user.getPhone()).isNotEmpty().hasSize(1);
        assertThat(user.getPhone().get(0)).isEqualTo(testUser.getPhone().get(0));
        assertThat(user.getAddress()).isEqualTo(testUser.getAddress());
    }

    @Test
    @DisplayName("добавлять или обновлять пользователя в БД, если он там уже есть")
    void shouldCorrectInsertAndUpdateUserInDatabase() {
        //Проверяем добавление
        User testUser = new User(ZERO_ID, TEST_USER_NAME);
        Phone phone1 = new Phone(testUser, TEST_PHONE_01);
        Phone phone2 = new Phone(testUser, TEST_PHONE_02);
        Address address = new Address(testUser, TEST_ADDRESS_MAYAK);
        testUser.setAddress(address);
        testUser.setPhone(Arrays.asList(phone1, phone2));

        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(testUser);
        sessionManagerHibernate.commitSession();

        checkStatistic(User.class, 1, 0);
        checkStatistic(Phone.class, 2, 0);
        checkStatistic(Address.class, 1, 0);

        assertThat(testUser.getId()).isGreaterThan(ZERO_ID);
        assertThat(phone1.getId()).isNotNull();
        assertThat(phone2.getId()).isNotNull();
        assertThat(address.getId()).isNotNull();

        //Проверяем обновление в БД
        testUser.setName(TEST_USER_UPDATED_NAME);
        sessionManagerHibernate.beginSession();
        testUser.getAddress().setStreet("Новые Васюки");
        testUser.getPhone().get(0).setNumber(TEST_PHONE_03);
        userDaoHibernate.insertOrUpdate(testUser);
        sessionManagerHibernate.commitSession();

        checkStatistic(User.class, 1, 1);
        checkStatistic(Phone.class, 2, 1);
        checkStatistic(Address.class, 1, 1);

        testUser = loadUser(testUser.getId());

        assertThat(testUser.getName()).isEqualTo(TEST_USER_UPDATED_NAME);
        assertThat(testUser.getAddress().getStreet()).isEqualTo(TEST_ADDRESS_NEW_VASUIKI);
        assertThat(testUser.getPhone().get(0).getNumber()).isEqualTo(TEST_PHONE_03);
    }

    @DisplayName("возвращать менеджер сессий")
    @Test
    void getSessionManager() {
        assertThat(userDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
    }

    private void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    private User loadUser(long userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, userId);
            Hibernate.initialize(user.getPhone());
            Hibernate.initialize(user.getAddress());
            return user;
        }
    }

    protected EntityStatistics getUserStatistics(Class entityClass) {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(entityClass.getName());
    }

    protected void checkStatistic(Class entityClass, int insertCount, int updateCount) {
        EntityStatistics statistics = getUserStatistics(entityClass);
        assertThat(statistics.getInsertCount()).isEqualTo(insertCount);
        assertThat(statistics.getUpdateCount()).isEqualTo(updateCount);
    }
}