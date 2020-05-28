package com.chupaYchups.hibernate.dao;

import com.chupaYchups.core.dao.EntityDao;
import com.chupaYchups.core.sessionmanager.SessionManager;

import java.util.Optional;

public class EntityDaoHibernate<T> implements EntityDao<T> {

    @Override
    public Optional<T> findById(long id) {
        return Optional.empty();
    }

    @Override
    public long save(T entity) {
        return 0;
    }

    @Override
    public void update(T entity) {

    }

    @Override
    public SessionManager getSessionManager() {
        return null;
    }
}
