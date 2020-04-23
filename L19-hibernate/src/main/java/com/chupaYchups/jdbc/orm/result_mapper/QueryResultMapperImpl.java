package com.chupaYchups.jdbc.orm.result_mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QueryResultMapperImpl<T> implements QueryResultMapper<T> {

    private Class<T> cls;

    public QueryResultMapperImpl(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public T mapResultToObject(ResultSet resultSet, List<String> fieldNames) {
        T createdObject = null;
        try {
            if (resultSet.next()) {
                createdObject = cls.getConstructor().newInstance();
                for (int i = 0; i < fieldNames.size(); i++) {
                    Field field = cls.getDeclaredField(fieldNames.get(i));
                    field.setAccessible(true);
                    field.set(createdObject, resultSet.getObject(i + 1, field.getType()));
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException("Error while mapping result to entity object", e);
        }
        return createdObject;
    }
}
