package ru.chupaYchups.core.orm.sql_generator;

import ru.chupaYchups.core.field_info.ClassFieldInfo;
import ru.chupaYchups.core.field_info.visitor.ClassFieldVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlGeneratorImpl<T> implements SqlGenerator<T> {

    public static final String SELECT_CLAUSE = "select";
    public static final String INSERT_CLAUSE = "insert into";
    public static final String UPDATE_CLAUSE = "update";
    public static final String SET = "set";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String WHITESPACE = " ";
    public static final String COMMA = ",";
    public static final String POINT = ".";
    public static final String FROM_CLAUSE = "from";
    public static final String WHERE_CLAUSE = "where";
    public static final String VALUES_CLAUSE = "values";
    public static final String EQUALS = "=";
    public static final String QUESTION = "?";

    private final Class<T> CLS;
    private final String TABLE_NAME;

    private  ClassFieldVisitor<ClassFieldInfo> visitor;

    private enum SqlStatementType {
        FIND_BY_ID,
        INSERT,
        UPDATE
    }

    //Кешируем запросы в разрезе одного генератора
    private Map<SqlStatementType, String> sqlStatementCache = new HashMap<>();

    public SqlGeneratorImpl(Class<T> cls,  ClassFieldVisitor<ClassFieldInfo> visitor) {
        this.CLS = cls;
        this.visitor = visitor;
        this.TABLE_NAME = CLS.getName().substring(CLS.getName().lastIndexOf(POINT) + 1).toLowerCase();
    }

    @Override
    public SqlOperationInfo getFindByIdQuery(long id) {
        return generateFindByIdQuery(id);
    }

    @Override
    public SqlOperationInfo getInsertStatement(T object) {
        return generateInsertStatement(object);
    }

    private SqlOperationInfo<Long> generateFindByIdQuery(Long id) {

        ClassFieldInfo classFieldInfo = visitor.inspectClass(CLS).get();
        SqlOperationInfo<Long> info = new SqlOperationInfo();
        Map<String, String> fieldValueMap = classFieldInfo.getFieldValuesMap();
        List<String> paramNameList = new ArrayList<>(fieldValueMap.keySet());

        info.setQuery(sqlStatementCache.computeIfAbsent(SqlStatementType.FIND_BY_ID, sqlStatementType -> new StringBuilder(SELECT_CLAUSE).append(WHITESPACE).
                append(paramNameList.stream().collect(Collectors.joining(","))).append(WHITESPACE).
                append(FROM_CLAUSE).append(WHITESPACE).
                append(TABLE_NAME).append(WHITESPACE).
                append(WHERE_CLAUSE).append(WHITESPACE).
                append(classFieldInfo.getPrimaryKeyFieldName()).append(WHITESPACE).
                append(EQUALS).append(WHITESPACE).
                append(QUESTION).toString()));

        info.setParameter(id);
        info.setParameterNameList(paramNameList);
        info.setPrimaryKeyFieldName(classFieldInfo.getPrimaryKeyFieldName());

        return info;
    }

    private SqlOperationInfo<List<String>> generateInsertStatement(T obj) {

        ClassFieldInfo classFieldInfo = visitor.inspectObject(obj).get();
        SqlOperationInfo<List<String>> info = new SqlOperationInfo();
        Map<String, String> fieldValueMap = classFieldInfo.getFieldValuesMap();
        fieldValueMap.remove(classFieldInfo.getPrimaryKeyFieldName());

        info.setQuery(sqlStatementCache.computeIfAbsent(SqlStatementType.INSERT, sqlStatementType -> new StringBuilder(INSERT_CLAUSE).append(WHITESPACE).
            append(TABLE_NAME).append(WHITESPACE).
            append(LEFT_BRACKET).append(WHITESPACE).
            append(fieldValueMap.keySet().stream().collect(Collectors.joining(","))).append(WHITESPACE).
            append(RIGHT_BRACKET).append(WHITESPACE).
            append(VALUES_CLAUSE).
            append(LEFT_BRACKET).append(WHITESPACE).
            append(fieldValueMap.keySet().stream().map(s -> "?").collect(Collectors.joining(","))).append(WHITESPACE).
            append(RIGHT_BRACKET).toString()));

        info.setParameter(fieldValueMap.values().stream().collect(Collectors.toList()));
        info.setPrimaryKeyFieldName(classFieldInfo.getPrimaryKeyFieldName());

        return info;
    }

    @Override
    public SqlOperationInfo<List<String>> getUpdateStatement(T object) {

        ClassFieldInfo classFieldInfo = visitor.inspectObject(object).get();
        SqlOperationInfo<List<String>> info = new SqlOperationInfo<>();
        Map<String, String> fieldValueMap = classFieldInfo.getFieldValuesMap();
        String primaryKeyValue = fieldValueMap.remove(classFieldInfo.getPrimaryKeyFieldName());

        info.setQuery(sqlStatementCache.computeIfAbsent(SqlStatementType.UPDATE, sqlStatementType -> new StringBuilder(UPDATE_CLAUSE).append(WHITESPACE).
            append(TABLE_NAME).append(WHITESPACE).
            append(SET).append(WHITESPACE).
            append(fieldValueMap.keySet().stream().
                map(fieldName -> fieldName + EQUALS + QUESTION).
                collect(Collectors.joining(COMMA))).append(WHITESPACE).
            append(WHERE_CLAUSE).append(WHITESPACE).
            append(classFieldInfo.getPrimaryKeyFieldName()).append(WHITESPACE).
            append(EQUALS).append(WHITESPACE).
            append(QUESTION).toString()));

        List<String> parameterValueList = fieldValueMap.values().stream().collect(Collectors.toList());
        parameterValueList.add(primaryKeyValue);
        info.setParameter(parameterValueList);

        return info;
    }
}
