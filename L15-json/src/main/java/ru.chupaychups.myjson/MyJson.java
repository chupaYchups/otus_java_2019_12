package ru.chupaychups.myjson;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyJson {

    public static String toJson(Object object) {
        JsonValue jsonStructure = createJsonStructure(object);
        return convertToString(jsonStructure);
    }

    private static String convertToString(JsonValue JsonValue) {
        return JsonValue.toString();
    }

    private static JsonValue createJsonStructure(Object object) {
        Class objectClass = object.getClass();
        JsonValue structure;
        if (objectClass.isArray()) {
            structure =  processArray(object).build();
        } else if (Collection.class.isAssignableFrom(objectClass)) {
            structure = processCollection(object).build();
        } else {
            structure = processObject(object).build();
        }
        return structure;
    }

    private static JsonArrayBuilder processArray(Object arrayObject) {
        int arrayLength = Array.getLength(arrayObject);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(arrayObject, i);
            arrayBuilder.add(processObject(element));
        }
        return arrayBuilder;
    }

    private static JsonArrayBuilder processCollection(Object collectionObj) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ((Collection) collectionObj).forEach(o -> arrayBuilder.add(processObject(o)));
        return arrayBuilder;
    }

    private static JsonObjectBuilder processObject(Object object) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        Class objClass = object.getClass();
        if (Number.class.isAssignableFrom(objClass)) {
            return objectBuilder.add("", Json.createValue(new BigDecimal(object.toString())));
        }
        return processObjectFields(object, objectBuilder, objClass);
    }

    private static JsonObjectBuilder processObjectFields(Object object, JsonObjectBuilder objectBuilder, Class objClass) {
        List<Field> fieldList = Arrays.stream(objClass.getDeclaredFields()).
            filter(field -> !Modifier.isStatic(field.getModifiers())).
            collect(Collectors.toList());
        fieldList.forEach(field -> {
            field.setAccessible(true);
            try {
                Class fieldClass = field.getType();
                if (Number.class.isAssignableFrom(fieldClass)) {
                    objectBuilder.add(field.getName(), new BigDecimal(field.get(object).toString()));
                } else if (String.class.isAssignableFrom(fieldClass)) {
                    objectBuilder.add(field.getName(), field.get(object).toString());
                } else if (fieldClass.isArray()) {
                    objectBuilder.add(field.getName(), processArray(object));
                } else if (Collection.class.isAssignableFrom(fieldClass)) {
                    objectBuilder.add(field.getName(), processCollection(object));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot get field value " + field.getName());
            }
        });
        return objectBuilder;
    }
}
