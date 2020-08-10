package ru.chupaYchups.core.field_info.visitor;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Паттерн визитор - по полям класса,
 * производит глубокий обход полей класса, с созданием операций для каждого
 * поля.
 */
public abstract class ClassFieldVisitor <T> {

    public Supplier<T> inspectObject(Object object) {
        if (object == null) {
            return getNullRootObjectOperation();
        }
        return processObject(object, null);
    }

    public Supplier<T> inspectClass(Class cls) {
        Constructor constructor;
        Object objectToInspect;
        try {
            constructor = cls.getConstructor();
            objectToInspect = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Entity class must have non parameter constructor");
        } catch (ReflectiveOperationException re) {
            throw new RuntimeException("Error while trying to instantiate class " + cls.getName(), re);
        }
        return processObject(objectToInspect, null);
    }

    //Основная логика обращения с конкретным узлом
    private Supplier<T> processObject(Object object, Field field) {
        Supplier<T> returnOp;
        if (object == null) {
            return getNullObjectOperation(field);
        }
        Class objectClass = object.getClass();
        if (objectClass.isArray()) {
            returnOp = processArray(object, field);
        } else if (Collection.class.isAssignableFrom(objectClass)) {
            returnOp = processCollection(object, field);
        } else {
            if (isPrimitiveTypeOperation(objectClass)) {
                returnOp = processPrimitiveType(object, field);
            } else {
                returnOp = processCustomObject(object, field);
            }
        }
        return returnOp;
    }

    private Supplier processArray(Object arrayObject, Field field) {
        List<Supplier<T>> elementOperationList = new ArrayList<>();
        int arrayLength = Array.getLength(arrayObject);
        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(arrayObject, i);
            elementOperationList.add(processObject(element, null));
        }

        return getArrayOperation(elementOperationList, field);
    }

    private Supplier processCollection(Object collectionObj, Field field) {
        List<Supplier<T>> elementOperationList = new ArrayList<>();
        ((Collection)collectionObj).forEach(o -> {
            elementOperationList.add(processObject(o, null));
        });

        return getCollectionOperation(elementOperationList, field);
    }

    private Supplier processCustomObject(Object object, Field fld) {
        Class objectClass = object.getClass();
        List<Supplier<T>> elementOperationList = new ArrayList<>();

        List<Field> fieldList = Arrays.stream(objectClass.getDeclaredFields()).
                filter(field -> !Modifier.isStatic(field.getModifiers())).
                collect(Collectors.toList());

        fieldList.forEach(field -> {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                elementOperationList.add(processObject(fieldValue, field));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot get field value " + field.getName());
            }
        });

        return getCustomObjectOperation(elementOperationList, fld);
    }

    private Supplier<T> processPrimitiveType(Object object, Field field) {
        return getPrimitiveTypeOperation(object, field);
    }

    public abstract boolean isPrimitiveTypeOperation(Class cls);
    public abstract Supplier<T> getPrimitiveTypeOperation(Object object, Field field);
    public abstract Supplier<T> getArrayOperation(List<Supplier<T>> opList, Field field);
    public abstract Supplier<T> getCollectionOperation(List<Supplier<T>> opList, Field field);
    public abstract Supplier<T> getCustomObjectOperation(List<Supplier<T>> opList, Field field);
    public abstract Supplier<T> getNullObjectOperation(Field field);
    public abstract Supplier<T> getNullRootObjectOperation();
}
