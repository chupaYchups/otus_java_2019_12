package ru.chupaYchups.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.core.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class ExecutorDemo {
  private static final String URL = "jdbc:h2:mem:";
  private static Logger logger = LoggerFactory.getLogger(ExecutorDemo.class);

  public static void main(String[] args) throws SQLException {
    ExecutorDemo demo = new ExecutorDemo();

    try (Connection connection = DriverManager.getConnection(URL)) {
      connection.setAutoCommit(false);
      demo.createTable(connection);

      DbExecutor<User> executor = new DbExecutor<>();
      long userId = executor.insertRecord(connection, "insert into user(name) values (?)", Collections.singletonList("testUserName"));
      logger.info("created user:{}", userId);
      connection.commit();

      Optional<User> user = executor.selectRecord(connection, "select id, name from user where id  = ?", userId, resultSet -> {
        try {
          if (resultSet.next()) {
            return new User(resultSet.getLong("id"), resultSet.getString("name"));
          }
        } catch (SQLException e) {
          logger.error(e.getMessage(), e);
        }
        return null;
      });
      System.out.println(user);
    }
  }


  private void createTable(Connection connection) throws SQLException {
    try (PreparedStatement pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50))")) {
      pst.executeUpdate();
    }
  }


}
