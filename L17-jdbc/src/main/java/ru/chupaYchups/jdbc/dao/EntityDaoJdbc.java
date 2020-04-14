package ru.chupaYchups.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.core.dao.EntityDao;
import ru.chupaYchups.core.dao.UserDaoException;
import ru.chupaYchups.core.sessionmanager.SessionManager;
import ru.chupaYchups.jdbc.DbExecutor;
import ru.chupaYchups.jdbc.orm.result_mapper.QueryResultMapper;
import ru.chupaYchups.jdbc.orm.sql_generator.SqlGenerator;
import ru.chupaYchups.jdbc.orm.sql_generator.SqlOperationInfo;
import ru.chupaYchups.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class EntityDaoJdbc<T> implements EntityDao<T> {
  private static Logger logger = LoggerFactory.getLogger(EntityDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final DbExecutor<T> dbExecutor;
  private final SqlGenerator<T> sqlGenerator;
  private final QueryResultMapper<T> resultMapper;

  public EntityDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor, SqlGenerator<T> sqlGenerator, QueryResultMapper<T> resultMapper) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
    this.sqlGenerator = sqlGenerator;
    this.resultMapper = resultMapper;
  }


  @Override
  public Optional<T> findById(long id) {
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
  public long save(T entity) {
    try {
      SqlOperationInfo<List<String>> operationInfo = sqlGenerator.getInsertStatement(entity);
      long id = dbExecutor.insertRecord(getConnection(), operationInfo.getQuery(), operationInfo.getParameter());
      //user.setId(id);
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
