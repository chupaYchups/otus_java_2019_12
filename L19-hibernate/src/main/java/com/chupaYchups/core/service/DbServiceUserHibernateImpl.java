package com.chupaYchups.core.service;

import com.chupaYchups.core.dao.UserDao;
import com.chupaYchups.core.model.User;
import com.chupaYchups.core.sessionmanager.SessionManager;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DbServiceUserHibernateImpl implements DBServiceUser {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserHibernateImpl.class);

    private final UserDao userDao;

    public DbServiceUserHibernateImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.insertOrUpdate(user);
                long userId = user.getId();
                sessionManager.commitSession();
                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);
                logger.info("user: {}", userOptional.orElse(null));
                Hibernate.initialize(userOptional.get().getAddress());
                Hibernate.initialize(userOptional.get().getPhone());
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
