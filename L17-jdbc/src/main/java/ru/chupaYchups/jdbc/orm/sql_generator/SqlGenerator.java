package ru.chupaYchups.jdbc.orm.sql_generator;

public interface SqlGenerator<T> {
    String getFindByIdQuery();
    String getInsertStatement();
}
