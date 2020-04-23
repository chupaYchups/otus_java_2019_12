package com.chupaYchups.jdbc.orm.visitor;

import com.chupaYchups.jdbc.orm.annotation.Id;
import com.chupaYchups.jdbc.orm.visitor.exception.FieldInfoCollectorException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CollectFieldInfoVisitor extends ClassFieldVisitor<ClassFieldInfo> {

    private final static List<Class> primitiveTypeClassList = List.of(
            String.class, Integer.class, Character.class,
            Long.class, Double.class, Float.class, Short.class,
            Byte.class, Boolean.class);

    @Override
    public boolean isPrimitiveTypeOperation(Class cls) {
        return primitiveTypeClassList.
                stream().
                filter(aClass -> aClass.isAssignableFrom(cls)).
                findAny().isPresent();
    }

    @Override
    public Supplier<ClassFieldInfo> getPrimitiveTypeOperation(Object object, Field field) {
        return () -> {
            ClassFieldInfo fieldInfo = new ClassFieldInfo();
            fieldInfo.getFieldValuesMap().put(field.getName(), object != null ? object.toString() : null);
            if (field.isAnnotationPresent(Id.class)) {
                fieldInfo.setPrimaryKeyFieldName(field.getName());
            }
            return fieldInfo;
        };
    }

    @Override
    public Supplier<ClassFieldInfo> getNullObjectOperation(Field field) {
        return getPrimitiveTypeOperation(null, field);
    }

    @Override
    public Supplier<ClassFieldInfo> getArrayOperation(List<Supplier<ClassFieldInfo>> opList, Field field) {
        throw new UnsupportedOperationException("Array processing not implemented");
    }

    @Override
    public Supplier<ClassFieldInfo> getCollectionOperation(List<Supplier<ClassFieldInfo>> opList, Field field) {
        throw new UnsupportedOperationException("Collection processing not implemented");
    }

    @Override
    public Supplier<ClassFieldInfo> getCustomObjectOperation(List<Supplier<ClassFieldInfo>> opList, Field field) {
        // Значит не корневой
        if (field != null) {
            throw new UnsupportedOperationException("Deep inspection not implemented");
        }
        return () -> {
            ClassFieldInfo commonFieldInfo = new ClassFieldInfo();
            Map<String, String> commonFieldValuesMap = commonFieldInfo.getFieldValuesMap();
            for (Supplier<ClassFieldInfo> fieldInfoSupplier : opList) {
                ClassFieldInfo info = fieldInfoSupplier.get();
                if (info.getPrimaryKeyFieldName() != null) {
                    if (commonFieldInfo.getPrimaryKeyFieldName() != null) {
                        throw new FieldInfoCollectorException("Сannot uniquely indenify primary key");
                    }
                    commonFieldInfo.setPrimaryKeyFieldName(info.getPrimaryKeyFieldName());
                }
                commonFieldValuesMap.putAll(info.getFieldValuesMap());
            }
            if (commonFieldInfo.getPrimaryKeyFieldName() == null) {
                throw new FieldInfoCollectorException("Cannot find primary key field");
            }
            return commonFieldInfo;
        };
    }

    @Override
    public Supplier<ClassFieldInfo> getNullRootObjectOperation() {
        throw new FieldInfoCollectorException("Value of object is null");
    }
}

