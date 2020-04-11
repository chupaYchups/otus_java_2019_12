package ru.chupaYchups.jdbc.orm;

import java.sql.ResultSet;

public class MyOrmMapper<T> implements OrmMapper {

    public MyOrmMapper() {
    }

    @Override
    public String generateFindByIdQuery() {
        return null;
    }

    @Override
    public T mapResultToObject(ResultSet resultSet) {
        return null;
    }
}
