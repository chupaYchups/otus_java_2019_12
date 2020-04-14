package ru.chupaYchups.core.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.core.dao.EntityDao;
import ru.chupaYchups.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceImpl<T> implements DbService<T> {
  private static Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);

  private final EntityDao entityDao;

  public DbServiceImpl(EntityDao userDao) {
    this.entityDao = userDao;
  }

  @Override
  public long create(T entity) {
    try (SessionManager sessionManager = entityDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        long id = entityDao.save(entity);
        sessionManager.commitSession();
        logger.info("created entity: {}", id);
        return id;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }


  @Override
  public Optional<T> load(long id) {
    try (SessionManager sessionManager = entityDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<T> entityOptional = entityDao.findById(id);
        logger.info("user: {}", entityOptional.orElse(null));
        return entityOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public void update(T entity) {

  }

  @Override
  public void createOrUpdate(T entity) {

  }
}
