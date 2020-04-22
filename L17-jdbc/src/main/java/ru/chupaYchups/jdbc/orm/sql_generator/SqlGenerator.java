package ru.chupaYchups.jdbc.orm.sql_generator;

import java.util.List;

public interface SqlGenerator<T> {
    SqlOperationInfo<Long> getFindByIdQuery(long id);
    SqlOperationInfo<List<String>> getInsertStatement(T object);
    SqlOperationInfo<List<String>> getUpdateStatement(T object);
}

