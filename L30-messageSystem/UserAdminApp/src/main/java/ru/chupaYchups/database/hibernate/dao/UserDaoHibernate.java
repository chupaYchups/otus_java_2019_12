package ru.chupaYchups.database.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.chupaYchups.database.dao.UserDao;
import ru.chupaYchups.database.dao.UserDaoException;
import ru.chupaYchups.database.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.chupaYchups.database.hibernate.sessionmanager.SessionManagerHibernate;
import ru.chupaYchups.database.model.User;
import ru.chupaYchups.database.sessionmanager.SessionManager;
import java.util.Optional;
import java.util.List;

@Repository
public class UserDaoHibernate implements UserDao {

    public static final int USER_LIST_MAX_SIZE = 100;
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.
                getHibernateSession().
                createQuery("select u from User u where u.login = ?1", User.class).
                setParameter(1, login).
                getSingleResult());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> findAllUsers() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.
                    getHibernateSession().
                    createQuery("select u from User u", User.class).
                    setMaxResults(USER_LIST_MAX_SIZE).
                    getResultList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
