package ru.chupaYchups.core.service;


import java.util.Optional;

public interface DbService<T> {

  void create(T entity);

  void update(T entity);

  void createOrUpdate(T entity);

  Optional<T> load(long id);
}
