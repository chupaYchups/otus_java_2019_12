package ru.chupaYchups.core.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.core.dao.EntityDao;
import ru.chupaYchups.core.sessionmanager.SessionManager;
import ru.chupaYchups.jdbc.orm.model.User;

import java.util.Optional;

public class DbServiceUser implements DbService<User> {
  private static Logger logger = LoggerFactory.getLogger(DbServiceUser.class);

  private final EntityDao<User> entityDao;

  public DbServiceUser(EntityDao<User> userDao) {
    this.entityDao = userDao;
  }

  @Override
  public void create(User user) {
    try (SessionManager sessionManager = entityDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        long id = entityDao.save(user);
        sessionManager.commitSession();
        logger.info("created entity: {}", id);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }


  @Override
  public Optional<User> load(long id) {
    try (SessionManager sessionManager = entityDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> entityOptional = entityDao.findById(id);
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
  public void update(User user) {
    try (SessionManager sessionManager = entityDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        entityDao.update(user);
        sessionManager.commitSession();
        logger.info("updated entity: {}", user);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public void createOrUpdate(User user) {
    try (SessionManager sessionManager = entityDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        if (user.getId() != null) {
          entityDao.update(user);
          logger.info("updated user: {}", user);
        } else {
          entityDao.save(user);
          logger.info("saved user: {}", user);
        }
        sessionManager.commitSession();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }
}
