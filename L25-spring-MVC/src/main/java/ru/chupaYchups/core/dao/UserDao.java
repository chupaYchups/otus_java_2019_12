package ru.chupaYchups.core.dao;

import ru.chupaYchups.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<List<User>> findAllUsers();

    Optional<User> findByLogin(String userName);

    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);
}
