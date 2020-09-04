package ru.chupaYchups.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.cachehw.HwCache;
import ru.chupaYchups.core.dao.UserDao;
import ru.chupaYchups.core.model.User;
import java.util.List;
import java.util.Optional;

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
        return 0;
    }

    @Override
    public Optional<User> getUser(long id) {
        return null;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return null;
    }

    @Override
    public Optional<List<User>> findAllUsers() {
        return null;
    }
}
