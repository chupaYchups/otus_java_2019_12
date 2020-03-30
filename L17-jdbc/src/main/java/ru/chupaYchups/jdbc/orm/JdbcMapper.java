package ru.chupaYchups.jdbc.orm;

import ru.chupaYchups.core.orm.OrmMapper;
import ru.chupaYchups.jdbc.DbExecutor;

public class JdbcMapper<T> implements OrmMapper<T> {

    private DbExecutor<T> dbExecutor;

    public JdbcMapper() {
        this.dbExecutor = dbExecutor;
    }

    @Override
    public void create(T objectData) {

    }

    @Override
    public void update(T objectData) {

    }

    @Override
    public void createOrUpdate(T objectData) {

    }

    @Override
    public T load(long id, Class clazz) {
        return null;
    }
}
