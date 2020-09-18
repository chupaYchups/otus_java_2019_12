package ru.chupaYchups.core.service;

import ru.chupaYchups.core.dao.UserDao;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chupaYchups.cachehw.HwCache;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;


@Service
public class DbServiceUserImpl implements DBServiceUser {

    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    private HwCache<String, User> cache;

    public DbServiceUserImpl(UserDao userDao, HwCache<String, User> cache) {
        this.userDao = userDao;
        this.cache = cache;
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
        return findUser(id, ident -> userDao.findById(ident));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return findUser(login, lgn -> userDao.findByLogin(lgn));
    }

    @Override
    public Optional<List<User>> findAllUsers() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<List<User>> userListOptional = userDao.findAllUsers();
                logger.info("user list size: {}", userListOptional.map(users -> users.size()).orElse(null));
                return userListOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    private <T> Optional<User> findUser(T paramToFind, Function<T, Optional<User>> findOp) {
        Optional<User> cacheUserOptional = Optional.ofNullable(cache.get(String.valueOf(paramToFind)));
        if (cacheUserOptional.isPresent()) {
            return cacheUserOptional;
        }
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = findOp.apply(paramToFind);
                logger.info("user: {}", userOptional.orElse(null));
                userOptional.ifPresent(user -> {
                    Hibernate.initialize(userOptional.get().getAddress());
                    Hibernate.initialize(userOptional.get().getPhones());
                    cache.put(String.valueOf(paramToFind), userOptional.get());
                });
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
