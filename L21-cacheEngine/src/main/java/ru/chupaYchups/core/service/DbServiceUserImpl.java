package ru.chupaYchups.core.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.cachehw.HwCache;
import ru.chupaYchups.cachehw.MyCache;
import ru.chupaYchups.core.dao.UserDao;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.Optional;
import java.util.WeakHashMap;

public class DbServiceUserImpl implements DBServiceUser {

    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    private HwCache<String, User> cache;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
        this.cache = new MyCache<>(new WeakHashMap<>(), new ArrayList<>());
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
                cache.put(Long.toString(userId), user);
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
        Optional<User> cacheUserOptional = Optional.of(cache.get(Long.toString(id)));
        if (cacheUserOptional.isPresent()) {
            return cacheUserOptional;
        }
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);
                logger.info("user: {}", userOptional.orElse(null));
                Hibernate.initialize(userOptional.get().getAddress());
                Hibernate.initialize(userOptional.get().getPhone());
                cache.put(Long.toString(id), userOptional.get());
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
