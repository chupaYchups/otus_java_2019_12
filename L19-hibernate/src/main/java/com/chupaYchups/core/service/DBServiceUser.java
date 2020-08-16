package com.chupaYchups.core.service;

import com.chupaYchups.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

}
