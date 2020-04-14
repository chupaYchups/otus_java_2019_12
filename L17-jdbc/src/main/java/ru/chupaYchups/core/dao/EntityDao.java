package ru.chupaYchups.core.dao;

import ru.chupaYchups.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface EntityDao<T> {

  Optional<T> findById(long id);

  long save(T user);

  SessionManager getSessionManager();
}
