package ru.chupaychups.myjson;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class MyJson {

    private Map<Class, MyAdapter> typeAdapterMap;

    public MyJson() {
        typeAdapterMap = new HashMap<>();
        typeAdapterMap.put(Long.class, object -> Json.createValue((Long)object));
        typeAdapterMap.put(Integer.class, object -> Json.createValue((Integer) object));
        typeAdapterMap.put(Double.class, object -> Json.createValue((Double) object));
        typeAdapterMap.put(String.class, object -> Json.createValue((String) object));
        typeAdapterMap.put(Float.class, object -> Json.createValue((Float) object));
        typeAdapterMap.put(Short.class, object -> Json.createValue((Short) object));
        typeAdapterMap.put(Byte.class, object -> Json.createValue((Byte) object));
        typeAdapterMap.put(Character.class, object -> Json.createValue(((Character) object).toString()));
        typeAdapterMap.put(Boolean.class, object -> ((Boolean)object) ? JsonValue.TRUE : JsonValue.FALSE);
    }

    public String toJson(Object object) {
        JsonValue jsonStructure = object != null ? createJsonStructure(object) : JsonValue.NULL;
        return convertToString(jsonStructure);
    }

    private String convertToString(JsonValue JsonValue) {
        return JsonValue.toString();
    }

    private JsonValue createJsonStructure(Object object) {
        Class objectClass = object.getClass();
        JsonValue structure;
        if (objectClass.isArray()) {
            structure = processArray(object).build();
        } else if (Collection.class.isAssignableFrom(objectClass)) {
            structure = processCollection(object).build();
        } else {
            var adapter = typeAdapterMap.get(objectClass);
            if (adapter != null) {
                structure = adapter.apply(object);
            } else {
                structure = processCustomObject(object).build();
            }
        }
        return structure;
    }

    private void processObject(Object object, JsonArrayBuilder arrayBuilder) {
        var adapter = typeAdapterMap.get(object.getClass());
        if (adapter != null) {
            arrayBuilder.add(adapter.apply(object));
        } else {
            arrayBuilder.add(processCustomObject(object));
        }
    }

    private JsonArrayBuilder processArray(Object arrayObject) {
        int arrayLength = Array.getLength(arrayObject);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(arrayObject, i);
            processObject(element, arrayBuilder);
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder processCollection(Object collectionObj) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ((Collection) collectionObj).forEach(o -> processObject(o, arrayBuilder));
        return arrayBuilder;
    }

    private JsonObjectBuilder processCustomObject(Object object) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        Class objClass = object.getClass();
        return processObjectFields(object, objectBuilder, objClass);
    }

    private JsonObjectBuilder processObjectFields(Object object, JsonObjectBuilder objectBuilder, Class objClass) {

        List<Field> fieldList = Arrays.stream(objClass.getDeclaredFields()).
                filter(field -> !Modifier.isStatic(field.getModifiers())).
                collect(Collectors.toList());

        fieldList.forEach(field -> {
            field.setAccessible(true);
            try {
                Class fieldClass = field.getType();
                Object fieldValue = field.get(object);
                if (fieldClass.isArray()) {
                    objectBuilder.add(field.getName(), processArray(fieldValue));
                } else if (Collection.class.isAssignableFrom(fieldClass)) {
                    objectBuilder.add(field.getName(), processCollection(fieldValue));
                } else {
                    var adapter = typeAdapterMap.get(fieldClass);
                    if (adapter != null) {
                        objectBuilder.add(field.getName(), adapter.apply(fieldValue));
                    } else {
                        objectBuilder.add(field.getName(), processCustomObject(fieldValue));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot get field value " + field.getName());
            }
        });

        return objectBuilder;
    }

    @FunctionalInterface
    private interface MyAdapter {
        JsonValue apply(Object object);
    }
}
