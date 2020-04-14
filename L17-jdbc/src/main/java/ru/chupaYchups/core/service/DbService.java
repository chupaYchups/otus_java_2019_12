package ru.chupaYchups.core.service;


import java.util.Optional;

public interface DbService<T> {

  long create(T user);

  void update(T user);

  void createOrUpdate(T user);

  Optional<T> load(long id);
}
