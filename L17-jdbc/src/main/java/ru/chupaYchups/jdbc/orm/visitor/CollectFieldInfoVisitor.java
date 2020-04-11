package ru.chupaYchups.jdbc.orm.visitor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CollectFieldInfoVisitor extends ClassFieldVisitor {


    private Map<String, ClassFieldInfo> fieldInfoMap;

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
    public ProcessOperation getPrimitiveTypeOperation(Object object) {
        return null;
    }

    @Override
    public ProcessOperation getArrayOperation(List opList) {
        return null;
    }

    @Override
    public ProcessOperation getCollectionOperation(List opList) {
        return null;
    }

    @Override
    public ProcessOperation getCustomObjectOperation(List opList) {




        return null;
    }

    @Override
    public ProcessOperation getFieldOperation(Field field, ProcessOperation childOp) {
        return null;
    }

    @Override
    public ProcessOperation getNullObjectOperation() {
        return null;
    }

    @Override
    public ProcessOperation getNullRootObjectOperation() {
        return null;
    }
}

