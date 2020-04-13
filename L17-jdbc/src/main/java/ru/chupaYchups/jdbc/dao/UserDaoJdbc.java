package ru.chupaYchups.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.core.dao.UserDao;
import ru.chupaYchups.core.dao.UserDaoException;
import ru.chupaYchups.core.sessionmanager.SessionManager;
import ru.chupaYchups.jdbc.DbExecutor;
import ru.chupaYchups.jdbc.orm.model.User;
import ru.chupaYchups.jdbc.orm.result_mapper.QueryResultMapper;
import ru.chupaYchups.jdbc.orm.sql_generator.SqlGenerator;
import ru.chupaYchups.jdbc.orm.sql_generator.SqlOperationInfo;
import ru.chupaYchups.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final DbExecutor<User> dbExecutor;
  private final SqlGenerator<User> sqlGenerator;
  private final QueryResultMapper<User> resultMapper;

  public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor, SqlGenerator<User> sqlGenerator, QueryResultMapper<User> resultMapper) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
    this.sqlGenerator = sqlGenerator;
    this.resultMapper = resultMapper;
  }


  @Override
  public Optional<User> findById(long id) {
    try {
      SqlOperationInfo<Long> operationInfo = sqlGenerator.getFindByIdQuery(id);
      return dbExecutor.selectRecord(getConnection(), operationInfo.getQuery(),
          operationInfo.getParameter(), resultSet -> resultMapper.mapResultToObject(resultSet, operationInfo.getParameterNameList()));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }


  @Override
  public long saveUser(User user) {
    try {
      SqlOperationInfo<List<String>> operationInfo = sqlGenerator.getInsertStatement(user);
      long id = dbExecutor.insertRecord(getConnection(), operationInfo.getQuery(), operationInfo.getParameter());
      user.setId(id);
      return id;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }

  private Connection getConnection() {
    return sessionManager.getCurrentSession().getConnection();
  }
}
