package ru.chupaYchups.core.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.chupaYchups.core.dao.EntityDao;
import ru.chupaYchups.h2.DataSourceH2;
import ru.chupaYchups.jdbc.DbExecutor;
import ru.chupaYchups.jdbc.dao.EntityDaoJdbc;
import ru.chupaYchups.jdbc.orm.model.Account;
import ru.chupaYchups.jdbc.orm.model.User;
import ru.chupaYchups.jdbc.orm.result_mapper.QueryResultMapper;
import ru.chupaYchups.jdbc.orm.result_mapper.QueryResultMapperImpl;
import ru.chupaYchups.jdbc.orm.sql_generator.SqlGenerator;
import ru.chupaYchups.jdbc.orm.sql_generator.SqlGeneratorImpl;
import ru.chupaYchups.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

class DbServiceUserImplTest {

    public static final String CREATE_TABLE_USER_SCRIPT = "create table user(" +
        "id bigint(20) NOT NULL auto_increment," +
        " name varchar(255)," +
        " age int(3))";
    public static final String CREATE_TABLE_ACCOUNT_SCRIPT = "create table account(" +
        "no bigint(20) NOT NULL auto_increment," +
        "type varchar(255)," +
        "rest number)";

    private SessionManagerJdbc sessionManager;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new DataSourceH2();
        sessionManager = new SessionManagerJdbc(dataSource);
    }

    private void createTable(DataSource dataSource, String creation_script) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(creation_script)) {
             pst.executeUpdate();
        }
        System.out.println("table created");
    }

    @Test
    void testThatCRUOperationsWithUserWorkCorrectly() throws SQLException {
        createTable(dataSource, CREATE_TABLE_USER_SCRIPT);
        DbExecutor<User> dbExecutor = new DbExecutor<>();

        SqlGenerator sqlGenerator = new SqlGeneratorImpl(User.class);
        QueryResultMapper resultMapper = new QueryResultMapperImpl(User.class);

        EntityDao<User> userDao = new EntityDaoJdbc<>(sessionManager, dbExecutor, sqlGenerator, resultMapper);

        DbService<User> dbService = new DbServiceUser(userDao);

        User userToSave = new User();
        userToSave.setAge(30L);
        userToSave.setName("lol");

        dbService.create(userToSave);

        Optional<User> userReturned = dbService.load(userToSave.getId());

        Assertions.assertThat(userReturned.isPresent());
        Assertions.assertThat(userReturned.get()).isEqualTo(userToSave);

        userToSave.setName("new name");
        dbService.update(userToSave);

        userReturned = dbService.load(userToSave.getId());

        Assertions.assertThat(userReturned.isPresent());
        Assertions.assertThat(userReturned.get()).isEqualTo(userToSave);

        User oneMoreUser = new User();
        oneMoreUser.setAge(40L);
        oneMoreUser.setName("one more user name");

        dbService.createOrUpdate(oneMoreUser);

        userReturned = dbService.load(oneMoreUser.getId());

        Assertions.assertThat(userReturned.isPresent());
        Assertions.assertThat(userReturned.get()).isEqualTo(oneMoreUser);

        oneMoreUser.setName("new one more user name");
        dbService.update(oneMoreUser);

        userReturned = dbService.load(oneMoreUser.getId());

        Assertions.assertThat(userReturned.isPresent());
        Assertions.assertThat(userReturned.get()).isEqualTo(oneMoreUser);
    }

    @Test
    void testThatCRUOperationsWithAccountWorkCorrectly() throws SQLException {
        createTable(dataSource, CREATE_TABLE_ACCOUNT_SCRIPT);

        DbExecutor<User> dbExecutor = new DbExecutor<>();

        SqlGenerator sqlGenerator = new SqlGeneratorImpl(Account.class);
        QueryResultMapper resultMapper = new QueryResultMapperImpl(Account.class);

        EntityDao<Account> entityDao = new EntityDaoJdbc<>(sessionManager, dbExecutor, sqlGenerator, resultMapper);

        DbService<Account> dbService = new DbServiceAccount(entityDao);

        Account accountToSave = new Account();
        accountToSave.setType("testAccType");
        accountToSave.setRest(7777777L);

        dbService.create(accountToSave);

        Optional<Account> accountReturned = dbService.load(1l);

        Assertions.assertThat(accountReturned.isPresent());
        Assertions.assertThat(accountReturned.get()).isEqualTo(accountToSave);

        Account oneMoreAccount = new Account();
        oneMoreAccount.setRest(40L);
        oneMoreAccount.setType("one more account type");

        dbService.createOrUpdate(oneMoreAccount);

        accountReturned = dbService.load(oneMoreAccount.getNo());

        Assertions.assertThat(accountReturned.isPresent());
        Assertions.assertThat(accountReturned.get()).isEqualTo(oneMoreAccount);

        oneMoreAccount.setType("new one more account type");
        dbService.update(oneMoreAccount);

        accountReturned = dbService.load(oneMoreAccount.getNo());

        Assertions.assertThat(accountReturned.isPresent());
        Assertions.assertThat(accountReturned.get()).isEqualTo(oneMoreAccount);
    }
}