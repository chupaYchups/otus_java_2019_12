package ru.chupaYchups.core.dao;

import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {

  Optional<User> findById(long id);

  long saveUser(User user);

  SessionManager getSessionManager();
}
