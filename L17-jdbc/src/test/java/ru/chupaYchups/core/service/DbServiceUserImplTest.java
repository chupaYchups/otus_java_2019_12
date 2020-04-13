package ru.chupaYchups.core.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.chupaYchups.core.dao.UserDao;
import ru.chupaYchups.h2.DataSourceH2;
import ru.chupaYchups.jdbc.DbExecutor;
import ru.chupaYchups.jdbc.dao.UserDaoJdbc;
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
    void testThatSaveAndLoadUserWorkCorrectly() throws SQLException {
        createTable(dataSource, CREATE_TABLE_USER_SCRIPT);

        DbExecutor<User> dbExecutor = new DbExecutor<>();

        SqlGenerator sqlGenerator = new SqlGeneratorImpl(User.class);
        QueryResultMapper resultMapper = new QueryResultMapperImpl(User.class);

        UserDao userDao = new UserDaoJdbc(sessionManager, dbExecutor, sqlGenerator, resultMapper);

        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        User userToSave = new User();
        userToSave.setAge(30L);
        userToSave.setName("lol");

        dbServiceUser.create(userToSave);

        Optional<User> userReturned = dbServiceUser.load(1l);

        Assertions.assertThat(userReturned.isPresent());
        Assertions.assertThat(userReturned.get()).isEqualTo(userToSave);
    }

    @Test
    void testThatSaveAndLoadAccountWorkCorrectly() throws SQLException {
/*        createTable(dataSource, CREATE_TABLE_USER_SCRIPT);

        DbExecutor<User> dbExecutor = new DbExecutor<>();

        SqlGenerator sqlGenerator = new SqlGeneratorImpl(User.class);
        QueryResultMapper resultMapper = new QueryResultMapperImpl(User.class);

        UserDao userDao = new UserDaoJdbc(sessionManager, dbExecutor, sqlGenerator, resultMapper);

        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        User userToSave = new User();
        userToSave.setAge(30L);
        userToSave.setName("lol");

        dbServiceUser.create(userToSave);

        Optional<User> userReturned = dbServiceUser.load(1l);

        Assertions.assertThat(userReturned.isPresent());
        Assertions.assertThat(userReturned.get()).isEqualTo(userToSave);*/
    }
}