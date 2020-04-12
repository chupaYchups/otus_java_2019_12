package ru.chupaYchups.core.service;


import ru.chupaYchups.jdbc.orm.model.User;

import java.util.Optional;

public interface DBServiceUser {

  long create(User user);

  void update(User user);

  void createOrUpdate(User user);

  Optional<User> load(long id);
}
