package ru.chupaYchups.jdbc.orm.visitor;

import java.util.ArrayList;
import java.util.List;

public class ClassFieldInfo {

    private String primaryKeyFieldName;
    private List<String> fieldNames;

    public ClassFieldInfo() {
        fieldNames = new ArrayList<>();
    }

    public String getPrimaryKeyFieldName() {
        return primaryKeyFieldName;
    }
    public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
        this.primaryKeyFieldName = primaryKeyFieldName;
    }
    public List<String> getFieldNames() {
        return fieldNames;
    }
    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }
}
