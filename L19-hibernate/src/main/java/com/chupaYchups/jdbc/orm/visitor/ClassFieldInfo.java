package com.chupaYchups.jdbc.orm.visitor;

import java.util.NavigableMap;
import java.util.TreeMap;

public class ClassFieldInfo {

    private String primaryKeyFieldName;
    private NavigableMap<String, String> fieldValuesMap;

    public ClassFieldInfo() {
        fieldValuesMap = new TreeMap<>();
    }

    public String getPrimaryKeyFieldName() {
        return primaryKeyFieldName;
    }
    public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
        this.primaryKeyFieldName = primaryKeyFieldName;
    }

    public NavigableMap<String, String> getFieldValuesMap() {
        return fieldValuesMap;
    }

    public void setFieldValuesMap(NavigableMap<String, String> fieldValuesMap) {
        this.fieldValuesMap = fieldValuesMap;
    }
}
