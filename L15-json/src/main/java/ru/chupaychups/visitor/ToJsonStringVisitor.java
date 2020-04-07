package ru.chupaychups.visitor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToJsonStringVisitor extends ClassFieldVisitor<String> {

    final Map<Class, Function<Object, String>> typeAdapterMap = Map.of(String.class, object -> "\"" + object + "\"",
        Integer.class, object -> object.toString(),
        Character.class, object -> "\"" + object.toString() + "\""
    );

    @Override
    public ProcessOperation<String> getPrimitiveTypeOperation(Object object) {
        return new ProcessOperation<String>(object) {
            @Override
            public String execute() {
                return typeAdapterMap.get(object.getClass()).apply(object);
            }
        };
    }

    @Override
    public ProcessOperation<String> getArrayOperation(List<ProcessOperation<String>> opList) {
        return new ProcessOperation<String>(opList) {
            @Override
            public String execute() {
                return "[" + childOperationsToString(childOperations) + "]";
            }
        };
    }

    //TODO аналогично тому, что делаеться с массивом
    @Override
    public ProcessOperation<String> getCollectionOperation(List<ProcessOperation<String>> opList) {
        return new ProcessOperation<String>(opList) {
            @Override
            public String execute() {
                return "[" + childOperationsToString(childOperations) + "]";
            }
        };
    }

/*    @Override
    public ProcessOperation<String> getRootObjectOperation(List<ProcessOperation<String>> opList) {
        return new ProcessOperation<String>(opList) {
            @Override
            public String execute() {
                return "{" + childOperationsToString(childOperations) + "}";
            }
        };
    }*/

    @Override
    public ProcessOperation<String> getCustomObjectOperation(List<ProcessOperation<String>> opList) {
        return new ProcessOperation<String>(opList) {
            @Override
            public String execute() {
                return "{" + childOperationsToString(childOperations) + "}";
            }
        };
    }

    @Override
    public ProcessOperation<String> getFieldOperation(Field field, ProcessOperation childOp) {
        return new ProcessOperation<String>(List.of(childOp)) {
            @Override
            public String execute() {
                return childOp == null ? "" : "\"" + field.getName() + "\"" + ":" + childOperationsToString(childOperations);
            }
        };
    }
    private String childOperationsToString(List<ProcessOperation<String>> childOperations) {
        return childOperations.stream().
                map(stringProcessOperation -> stringProcessOperation.execute()).
                collect(Collectors.joining(","));
    }

    @Override
    public ProcessOperation<String> getNullObjectOperation() {
        return new ProcessOperation<String>(null) {
            @Override
            public String execute() {
                return "null";
            }
        };
    }
}
