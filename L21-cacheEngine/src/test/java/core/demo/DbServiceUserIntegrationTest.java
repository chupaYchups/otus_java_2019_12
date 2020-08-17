package core.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.chupaYchups.core.hibernate.HibernateUtils;
import ru.chupaYchups.core.hibernate.dao.UserDaoHibernate;
import ru.chupaYchups.core.hibernate.sessionmanager.SessionManagerHibernate;
import ru.chupaYchups.core.model.Address;
import ru.chupaYchups.core.model.Phone;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.core.service.DbServiceUserImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверяем что класс сервиса")
public class DbServiceUserIntegrationTest {

    private DBServiceUser dbServiceUser;
    private SessionFactory sessionFactory;

    public static final int ZERO_ID = 0;
    public static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";
    public static final String TEST_ADDRESS_NEW_VASIUKI = "Новые Васюки";
    public static final String TEST_PHONE_01 = "01";
    public static final String TEST_PHONE_02 = "02";

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML, Phone.class, Address.class, User.class);
        SessionManagerHibernate sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        UserDaoHibernate userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
        dbServiceUser = new DbServiceUserImpl(userDaoHibernate);
    }

    @Test
    @DisplayName("корректно находит пользователя по идентификатору")
    void shouldCorrectFindUserById() {
        User user = new User(0, "testUserName");
        Phone phone01 = new Phone(user, TEST_PHONE_01);
        Phone phone02 = new Phone(user, TEST_PHONE_02);
        Address address = new Address(user, TEST_ADDRESS_NEW_VASIUKI);
        user.setAddress(address);
        user.setPhone(Arrays.asList(phone01, phone02));
        saveUser(user);
        assertThat(user.getId()).isGreaterThan(ZERO_ID);

        Optional<User> foundUser = dbServiceUser.getUser(user.getId());

        assertThat(foundUser).isPresent().get().isEqualTo(user);
        assertThat(foundUser.get().getAddress()).isNotNull().isEqualTo(user.getAddress());
        assertThat(foundUser.get().getPhone()).isNotEmpty().hasSize(2).hasSameElementsAs(user.getPhone());
    }

    private void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }
}
