package ru.chupaYchups.service;

import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;

public class InitializationService {

    private DBServiceUser dbServiceUser;

    public InitializationService(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    public void init() {
        createSomeUsers();
    }

    private void createSomeUsers() {
        User user1 = new User(0, "Иванов", "ivanov", "111");
        User user2 = new User(0, "Петров", "petrov", "222");
        User user3 = new User(0, "Сидоров", "sidorov", "333");
        dbServiceUser.saveUser(user1);
        dbServiceUser.saveUser(user2);
        dbServiceUser.saveUser(user3);
    }
}
