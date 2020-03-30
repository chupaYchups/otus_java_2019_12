package ru.chupaYchups.core.orm;

public interface OrmMapper<T> {

    void create(T objectData);

    void update(T objectData);

    void createOrUpdate(T objectData);

    T load(long id, Class<T> clazz);
}
