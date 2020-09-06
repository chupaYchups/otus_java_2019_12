package ru.chupaYchups.service;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;

@Service
public class InitializationService {

    private DBServiceUser dbServiceUser;

    public InitializationService(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
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
