package com.chupaYchups.jdbc.orm.result_mapper;

import java.sql.ResultSet;
import java.util.List;

public interface QueryResultMapper<T> {
    T mapResultToObject(ResultSet resultSet, List<String> fieldNames);
}
