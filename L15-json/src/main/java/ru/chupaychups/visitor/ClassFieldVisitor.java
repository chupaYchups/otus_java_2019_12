package ru.chupaychups.visitor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Паттерн визитор - по полям класса,
 * производит глубокий обход полей класса, с созданием операций для каждого
 * поля.
*/
public abstract class ClassFieldVisitor <T> {

    public ProcessOperation<T> inspectObject(Object object) {
        return processObject(object, null);
    }

    //Основная логика обращения с конкретным узлом
    private ProcessOperation<T> processObject(Object object, Field field) {
        ProcessOperation<T> returnOp;
        if (object == null) {
            return getNullObjectOperation();
        }
        Class objectClass = object.getClass();
        if (objectClass.isArray()) {
            returnOp = processArray(object, field);
        } else if (Collection.class.isAssignableFrom(objectClass)) {
            returnOp = processCollection(object, field);
        } else {
            if (isPrimitiveType(objectClass)) {
                returnOp = processPrimitiveType(object, field);
            } else {
                returnOp = processCustomObject(object, field);
            }
        }
        return returnOp;
    }

    private ProcessOperation<T> processArray(Object arrayObject, Field field) {
        List<ProcessOperation<T>> elementOperationList = new ArrayList<>();
        int arrayLength = Array.getLength(arrayObject);
        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(arrayObject, i);
            elementOperationList.add(processObject(element, null));
        }

        return field != null ? getFieldOperation(field, getArrayOperation(elementOperationList)) : getArrayOperation(elementOperationList);
    }

    private ProcessOperation<T> processCollection(Object collectionObj, Field field) {
        List<ProcessOperation<T>> elementOperationList = new ArrayList<>();
        ((Collection)collectionObj).forEach(o -> {
            elementOperationList.add(processObject(o, null));
        });

        return field != null ? getFieldOperation(field, getCollectionOperation(elementOperationList)) : getCollectionOperation(elementOperationList);
    }

    private ProcessOperation<T> processCustomObject(Object object, Field customObjField) {
        Class objectClass = object.getClass();
        List<ProcessOperation<T>> elementOperationList = new ArrayList<>();

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

        return customObjField != null ? getFieldOperation(customObjField, getCustomObjectOperation(elementOperationList)) : getCustomObjectOperation(elementOperationList);
    }

    private ProcessOperation<T> processPrimitiveType(Object object, Field field) {
        return field != null ? getFieldOperation(field, getPrimitiveTypeOperation(object)) : getPrimitiveTypeOperation(object);
    }

    private boolean isPrimitiveType(Class objectClass) {
        return String.class.isAssignableFrom(objectClass) ||
               Number.class.isAssignableFrom(objectClass) ||
               Character.class.isAssignableFrom(objectClass);
    }

    public abstract class ProcessOperation<T> {

        private Object obj;
        protected List<ProcessOperation<T>> childOperations;

        ProcessOperation(Object object) {
            this.obj = object;
        }

        ProcessOperation(List<ProcessOperation<T>> childOperations) {
            this.childOperations = childOperations;
        }

        public abstract T execute();
    }

    public abstract ProcessOperation<T> getPrimitiveTypeOperation(Object object);
    public abstract ProcessOperation<T> getArrayOperation(List<ProcessOperation<T>> opList);
    public abstract ProcessOperation<T> getCollectionOperation(List<ProcessOperation<T>> opList);
//    public abstract ProcessOperation<T> getRootObjectOperation(List<ProcessOperation<T>> opList);
    public abstract ProcessOperation<T> getCustomObjectOperation(List<ProcessOperation<T>> opList);
    public abstract ProcessOperation<T> getFieldOperation(Field field, ProcessOperation childOp);
    public abstract ProcessOperation<T> getNullObjectOperation();
}
