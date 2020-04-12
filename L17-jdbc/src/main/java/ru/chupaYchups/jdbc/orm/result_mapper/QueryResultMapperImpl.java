package ru.chupaYchups.jdbc.orm.result_mapper;

import ru.chupaYchups.jdbc.orm.visitor.ClassFieldInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QueryResultMapperImpl<T> implements QueryResultMapper<T> {

    private Class<T> cls;
    private List<String> fields;

    public QueryResultMapperImpl(Class<T> cls, ClassFieldInfo classFieldInfo) {
        this.cls = cls;
        this.fields = classFieldInfo.getFieldNames();
    }

    @Override
    public T mapResultToObject(ResultSet resultSet) {
        T createdObject = null;
        try {
            if (resultSet.next()) {
                createdObject = cls.getConstructor().newInstance();
                for (int i = 0; i < fields.size(); i++) {
                    Field field = cls.getDeclaredField(fields.get(i));
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
