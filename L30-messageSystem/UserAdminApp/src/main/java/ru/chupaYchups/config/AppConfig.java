package ru.chupaYchups.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.chupaYchups.database.cachehw.HwCache;
import ru.chupaYchups.database.cachehw.MyCache;
import ru.chupaYchups.database.hibernate.HibernateUtils;
import ru.chupaYchups.database.model.Address;
import ru.chupaYchups.database.model.Phone;
import ru.chupaYchups.database.model.User;
import ru.chupaYchups.database.service.DBServiceUser;
import ru.chupaYchups.service.InitializationService;
import java.util.ArrayList;
import java.util.HashMap;

@Configuration
@ComponentScan
public class AppConfig {

    public static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML, Phone.class, Address.class, User.class);
    }

    @Bean(initMethod = "init")
    public InitializationService initializationService(DBServiceUser dbServiceUser) {
        return new InitializationService(dbServiceUser);
    }

    @Bean
    public HwCache<String, User> userCache() {
        return new MyCache<>(new HashMap<>(), new ArrayList<>());
    }
}
