package ru.chupaYchups.jdbc.orm.sql_generator;

import ru.chupaYchups.jdbc.orm.visitor.ClassFieldInfo;
import ru.chupaYchups.jdbc.orm.visitor.CollectFieldInfoVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlGeneratorImpl<T> implements SqlGenerator<T> {

    public static final String SELECT_CLAUSE = "select";
    public static final String INSERT_CLAUSE = "insert into";
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

    public SqlGeneratorImpl(Class<T> cls) {
        this.CLS = cls;
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
        ClassFieldInfo classFieldInfo = new CollectFieldInfoVisitor().inspectClass(CLS).get();
        SqlOperationInfo<Long> info = new SqlOperationInfo();
        Map<String, String> fieldValueMap = classFieldInfo.getFieldValuesMap();
        List<String> paramNameList = new ArrayList<>(fieldValueMap.keySet());

        info.setQuery(new StringBuilder(SELECT_CLAUSE).append(WHITESPACE).
                append(paramNameList.stream().collect(Collectors.joining(","))).append(WHITESPACE).
                append(FROM_CLAUSE).append(WHITESPACE).
                append(TABLE_NAME).append(WHITESPACE).
                append(WHERE_CLAUSE).append(WHITESPACE).
                append(classFieldInfo.getPrimaryKeyFieldName()).append(WHITESPACE).
                append(EQUALS).append(WHITESPACE).
                append(QUESTION).toString());
        info.setParameter(id);
        info.setParameterNameList(paramNameList);

        return info;
    }

    private SqlOperationInfo<List<String>> generateInsertStatement(T obj) {

        ClassFieldInfo classFieldInfo = new CollectFieldInfoVisitor().inspectObject(obj).get();
        SqlOperationInfo<List<String>> info = new SqlOperationInfo();
        Map<String, String> fieldValueMap = classFieldInfo.getFieldValuesMap();
        fieldValueMap.remove(classFieldInfo.getPrimaryKeyFieldName());

        info.setQuery( new StringBuilder(INSERT_CLAUSE).append(WHITESPACE).
                append(TABLE_NAME).append(WHITESPACE).
                append(LEFT_BRACKET).append(WHITESPACE).
                append(fieldValueMap.keySet().stream().collect(Collectors.joining(","))).append(WHITESPACE).
                append(RIGHT_BRACKET).append(WHITESPACE).
                append(VALUES_CLAUSE).
                append(LEFT_BRACKET).append(WHITESPACE).
                append(fieldValueMap.keySet().stream().map(s -> "?").collect(Collectors.joining(","))).append(WHITESPACE).
                append(RIGHT_BRACKET).toString());
        info.setParameter(fieldValueMap.values().stream().collect(Collectors.toList()));

        return info;
    }

/*    private String getFieldListString(List<String> fields, boolean withPrimaryKey) {
        var stream = classFieldInfo.getFieldValuesMap().keySet().stream();
        if (!withPrimaryKey) {
            stream = stream.filter(s -> !classFieldInfo.getPrimaryKeyFieldName().equals(s));
        }
        return stream.collect(Collectors.joining(COMMA));
    }

    private String maskFieldsWithQuestion(List<String> fields) {
        return fields.stream().map(s -> "?").collect(Collectors.joining(","));
    }*/

/*    private String getFieldNamesString(Map<String, String> fieldValues, String primaryKeyFieldName, boolean maskQuestion, boolean withPrimaryKey) {
        Stream<String> stream = fieldValues.keySet().stream();
        if (!withPrimaryKey) {
            stream.filter(s -> {!s.equals(primaryKeyFieldName)});
        }
        if (maskQuestion) {
            stream.map(s -> "?");
        }
        return stream.collect(Collectors.joining(","));
    }

    private String getFieldValuesString(Map<String, String> fieldValues, String primaryKeyFieldName) {
        return fieldValues.values().stream().
            map(stringStringEntry -> stringStringEntry.getValue()).
            collect(Collectors.joining(","));
    }*/

}
