package ru.chupaYchups.jdbc.orm.visitor;

import java.util.Map;

public class ClassFieldInfo {

    private String primaryKeyFieldName;

    private Map<String, String> fieldValueMap;

    public String getPrimaryKeyFieldName() {
        return primaryKeyFieldName;
    }

    public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
        this.primaryKeyFieldName = primaryKeyFieldName;
    }

    public Map<String, String> getFieldValueMap() {
        return fieldValueMap;
    }

    public void setFieldValueMap(Map<String, String> fieldValueMap) {
        this.fieldValueMap = fieldValueMap;
    }
}
