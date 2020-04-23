package com.chupaYchups.core.dao;

import com.chupaYchups.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface EntityDao<T> {

  Optional<T> findById(long id);

  long save(T entity);

  void update(T entity);

  SessionManager getSessionManager();
}
