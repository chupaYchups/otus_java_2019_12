package ru.chupaYchups.database.dao;

import ru.chupaYchups.database.model.User;
import ru.chupaYchups.database.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<List<User>> findAllUsers();

    Optional<User> findByLogin(String userName);

    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
