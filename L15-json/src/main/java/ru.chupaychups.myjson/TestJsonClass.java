package ru.chupaychups.myjson;

public class TestJsonClass {

    private String field1String;

    private Integer field2Integer;

    private int[] intArray;

    public TestJsonClass(String field1String, Integer field2Integer, int[] intArray) {
        this.field1String = field1String;
        this.field2Integer = field2Integer;
        this.intArray = intArray;
    }

    public String getField1String() {
        return field1String;
    }

    public void setField1String(String field1String) {
        this.field1String = field1String;
    }

    public Integer getField2Integer() {
        return field2Integer;
    }

    public void setField2Integer(Integer field2Integer) {
        this.field2Integer = field2Integer;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }
}
