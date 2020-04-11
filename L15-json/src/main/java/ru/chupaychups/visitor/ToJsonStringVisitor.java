package ru.chupaychups.visitor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToJsonStringVisitor extends ClassFieldVisitor<String> {

    final Map<Class, Function<Object, String>> primitiveTypeAdapterMap = Map.of(
            String.class, object -> "\"" + object + "\"",
            Integer.class, object -> object.toString(),
            Character.class, object -> "\"" + object.toString() + "\"",
            Long.class, object -> object.toString(),
            Double.class, object -> object.toString(),
            Float.class, object -> object.toString(),
            Short.class, object -> object.toString(),
            Byte.class, object -> object.toString(),
            Boolean.class, object -> object.toString()
    );

    @Override
    public boolean isPrimitiveTypeOperation(Class cls) {
        return cls != null && primitiveTypeAdapterMap.keySet().contains(cls);
    }

    @Override
    public ProcessOperation<String> getPrimitiveTypeOperation(Object object, Field field) {
        return new ProcessOperation<String>(object) {
            @Override
            public String execute() {
                return getFieldString(field) + primitiveTypeAdapterMap.get(object.getClass()).apply(object);
            }
        };
    }

    @Override
    public ProcessOperation<String> getArrayOperation(List<ProcessOperation<String>> opList, Field field) {
        return new ProcessOperation<String>(opList) {
            @Override
            public String execute() {
                return getFieldString(field) + "[" + childOperationsToString(childOperations) + "]";
            }
        };
    }

    @Override
    public ProcessOperation<String> getCollectionOperation(List<ProcessOperation<String>> opList, Field field) {
        return getArrayOperation(opList, field);
    }

    @Override
    public ProcessOperation<String> getCustomObjectOperation(List<ProcessOperation<String>> opList, Field field) {
        return new ProcessOperation<String>(opList) {
            @Override
            public String execute() {
                return getFieldString(field) + "{" + childOperationsToString(childOperations) + "}";
            }
        };
    }

    private String getFieldString(Field field) {
        if (field == null) {
            return "";
        }
        return  "\"" + field.getName() + "\"" + ":";
    }


    private String childOperationsToString(List<ProcessOperation<String>> childOperations) {
        return childOperations.stream().
                map(stringProcessOperation -> stringProcessOperation.execute()).
                filter(s -> !s.isEmpty()).
                collect(Collectors.joining(","));
    }

    @Override
    public ProcessOperation<String> getNullRootObjectOperation() {
        return new ProcessOperation<String>(null) {
            @Override
            public String execute() {
                return "null";
            }
        };
    }

    @Override
    public ProcessOperation<String> getNullObjectOperation() {
        return new ProcessOperation<String>(null) {
            @Override
            public String execute() {
                return "";
            }
        };
    }
}
