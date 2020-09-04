package ru.chupaYchups.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SessionFactory;
import ru.chupaYchups.cachehw.MyCache;
import ru.chupaYchups.core.dao.UserDao;
import ru.chupaYchups.core.hibernate.HibernateUtils;
import ru.chupaYchups.core.hibernate.dao.UserDaoHibernate;
import ru.chupaYchups.core.hibernate.sessionmanager.SessionManagerHibernate;
import ru.chupaYchups.core.model.Address;
import ru.chupaYchups.core.model.Phone;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.core.service.DbServiceUserImpl;
import ru.chupaYchups.web.server.UsersWebServer;
import ru.chupaYchups.web.server.UsersWebServerWithFilterBasedSecurity;
import ru.chupaYchups.web.services.TemplateProcessor;
import ru.chupaYchups.web.services.TemplateProcessorImpl;
import ru.chupaYchups.web.services.UserAuthService;
import ru.chupaYchups.web.services.UserAuthServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;

/*
* Демо работы сервера
*/
public class WebServerWithFilterBasedSecurityDemo {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML, Phone.class, Address.class, User.class);
        SessionManagerHibernate sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        UserDao userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
        DBServiceUser dbService = new DbServiceUserImpl(userDaoHibernate, new MyCache(new HashMap<>(), new ArrayList<>()));
        dbService.saveUser(new User(0, "Иван", "user", "111"));
        dbService.saveUser(new User(0, "Юлия", "user2", "111"));
        dbService.saveUser(new User(0, "Софья", "user3", "111"));

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbService);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, authService, dbService, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

}
