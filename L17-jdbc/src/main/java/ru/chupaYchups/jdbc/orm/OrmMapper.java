package ru.chupaYchups.jdbc.orm;

import java.sql.ResultSet;

public interface OrmMapper {

    String generateFindByIdQuery();

    Object mapResultToObject(ResultSet resultSet);
}
