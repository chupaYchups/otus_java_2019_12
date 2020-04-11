package ru.chupaYchups.jdbc.orm;

import java.sql.ResultSet;

public interface OrmMapper<T> {

    String generateFindByIdQuery();

    T mapResultToObject(ResultSet resultSet);
}
