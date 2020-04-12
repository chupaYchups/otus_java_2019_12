package ru.chupaYchups.jdbc.orm.sql_generator;

import ru.chupaYchups.jdbc.orm.visitor.ClassFieldInfo;

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

    private final ClassFieldInfo CLASS_FIELD_INFO;
    private final Class<T> CLS;
    private final String TABLE_NAME;

    public SqlGeneratorImpl(Class<T> cls, ClassFieldInfo classFieldInfo) {
        this.CLS = cls;
        this.TABLE_NAME = CLS.getName().substring(CLS.getName().lastIndexOf(POINT) + 1).toLowerCase();
        this.CLASS_FIELD_INFO = classFieldInfo;
    }

    @Override
    public String getFindByIdQuery() {
        return generateFindByIdQuery();
    }

    @Override
    public String getInsertStatement() {
        return generateInsertStatement();
    }

    private String generateFindByIdQuery() {
        return new StringBuilder(SELECT_CLAUSE).append(WHITESPACE).
                append(getFieldList(false, true)).append(WHITESPACE).
                append(FROM_CLAUSE).append(WHITESPACE).
                append(TABLE_NAME).append(WHITESPACE).
                append(WHERE_CLAUSE).append(WHITESPACE).
                append(CLASS_FIELD_INFO.getPrimaryKeyFieldName()).append(WHITESPACE).
                append(EQUALS).append(WHITESPACE).
                append(QUESTION).toString();
    }

    private String generateInsertStatement() {
        return new StringBuilder(INSERT_CLAUSE).append(WHITESPACE).
                append(TABLE_NAME).append(WHITESPACE).
                append(LEFT_BRACKET).append(WHITESPACE).
                append(getFieldList(false, false)).append(WHITESPACE).
                append(RIGHT_BRACKET).append(WHITESPACE).
                append(VALUES_CLAUSE).
                append(LEFT_BRACKET).append(WHITESPACE).
                append(getFieldList(true, false)).append(WHITESPACE).
                append(RIGHT_BRACKET).toString();
    }

    private String getFieldList(boolean maskedByQuestion, boolean withPrimaryKey) {
        var stream = CLASS_FIELD_INFO.getFieldNames().stream();
        if (!withPrimaryKey) {
            stream = stream.filter(s -> !CLASS_FIELD_INFO.getPrimaryKeyFieldName().equals(s));
        }
        if (maskedByQuestion) {
            stream = stream.map(s -> QUESTION);
        }
        return stream.collect(Collectors.joining(COMMA));
    }
}
