package ru.chupaYchups.jdbc.orm.sql_generator;

import java.util.List;

public class SqlOperationInfo<T> {

    private String query;
    private T parameter;
    private List<String> parameterNameList;

    SqlOperationInfo() {}

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getParameterNameList() {
        return parameterNameList;
    }

    public void setParameterNameList(List<String> parameterNameList) {
        this.parameterNameList = parameterNameList;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }
}
