package ru.chupaYchups.jdbc.orm.result_mapper;

import java.sql.ResultSet;

public interface QueryResultMapper<T> {
    T mapResultToObject(ResultSet resultSet);
}
