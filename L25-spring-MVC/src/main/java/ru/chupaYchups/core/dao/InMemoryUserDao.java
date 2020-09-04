package ru.chupaYchups.core.dao;

import ru.chupaYchups.core.model.User;

import java.util.List;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {

    @Override
    public Optional<List<User>> findAllUsers() {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String userName) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        return 0;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void insertOrUpdate(User user) {

    }
}
