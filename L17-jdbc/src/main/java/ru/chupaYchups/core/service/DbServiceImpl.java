package ru.chupaYchups.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.core.dao.EntityDao;
import ru.chupaYchups.core.model.IdentifiedEntity;
import ru.chupaYchups.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceImpl<T extends IdentifiedEntity> implements DbService<T> {

    private static Logger logger = LoggerFactory.getLogger(DbServiceAccount.class);

    private final EntityDao<T> entityDao;

    public DbServiceImpl(EntityDao<T> userDao) {
        this.entityDao = userDao;
    }

    @Override
    public void create(T account) {
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long id = entityDao.save(account);
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
    public Optional<T> load(long id) {
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<T> entityOptional = entityDao.findById(id);
                logger.info("account: {}", entityOptional.orElse(null));
                return entityOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public void update(T account) {
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                entityDao.update(account);
                sessionManager.commitSession();
                logger.info("updated account: {}", account);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public void createOrUpdate(T account) {
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                if (account.getId() != null) {
                    entityDao.update(account);
                    logger.info("updated account: {}", account);
                } else {
                    entityDao.save(account);
                    logger.info("created account: {}", account);
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
