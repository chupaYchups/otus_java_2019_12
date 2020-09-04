package ru.chupaYchups.core.service;

import ru.chupaYchups.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    Optional<User> findByLogin(String login);

    Optional<List<User>> findAllUsers();
}
