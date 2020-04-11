package ru.chupaychups.visitor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
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
    public Supplier<String> getPrimitiveTypeOperation(Object object, Field field) {
        return () -> getFieldString(field) + primitiveTypeAdapterMap.get(object.getClass()).apply(object);
    }

    @Override
    public Supplier<String> getArrayOperation(List<Supplier<String>> opList, Field field) {
        return () -> getFieldString(field) + "[" + childOperationsToString(opList) + "]";
    }

    @Override
    public Supplier<String> getCollectionOperation(List<Supplier<String>> opList, Field field) {
        return getArrayOperation(opList, field);
    }

    @Override
    public Supplier<String> getCustomObjectOperation(List<Supplier<String>> opList, Field field) {
        return () -> getFieldString(field) + "{" + childOperationsToString(opList) + "}";
    }

    private String getFieldString(Field field) {
        if (field == null) {
            return "";
        }
        return  "\"" + field.getName() + "\"" + ":";
    }


    private String childOperationsToString(List<Supplier<String>> childOperations) {
        return childOperations.stream().
                map(stringSupplier -> stringSupplier.get()).
                filter(s -> !s.isEmpty()).
                collect(Collectors.joining(","));
    }

    @Override
    public Supplier<String> getNullRootObjectOperation() {
        return () -> "null";
    }

    @Override
    public Supplier<String> getNullObjectOperation() {
        return () -> "";
    }
}
