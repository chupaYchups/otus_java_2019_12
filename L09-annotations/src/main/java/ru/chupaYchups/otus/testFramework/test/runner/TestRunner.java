package ru.chupaYchups.otus.testFramework.test.runner;

import ru.chupaYchups.otus.testFramework.annotations.AfterEach;
import ru.chupaYchups.otus.testFramework.annotations.BeforeEach;
import ru.chupaYchups.otus.testFramework.annotations.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {

    private Class classOfTest;

    private final List<Class<? extends Annotation>> supportedAnnotationClasses = Arrays.asList(BeforeEach.class, AfterEach.class, Test.class);

    private Map<Class<? extends Annotation>, List<Method>> methodsWithAnnotation;

    public TestRunner(Class testClass) {
        this.classOfTest = testClass;
        collectTestMethods(testClass);
    }

    private void collectTestMethods(Class testClass) {
        methodsWithAnnotation = new HashMap<>();
        supportedAnnotationClasses.forEach(aClass -> methodsWithAnnotation.put(aClass, new ArrayList<>()));
        for (Method declaredMethod : testClass.getDeclaredMethods()) {
            for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
                if (supportedAnnotationClasses.contains(declaredAnnotation.annotationType())) {
                    methodsWithAnnotation.get(declaredAnnotation.annotationType()).add(declaredMethod);
                }
            }
        }
    }

    public void run() throws ReflectiveOperationException {
        try {
            for (Method testMethod : methodsWithAnnotation.get(Test.class)) {
                runOneTest(testMethod);
            }
        } catch (ReflectiveOperationException exc) {
            System.out.println("Test running failed with exception : " + exc.toString());
            exc.printStackTrace();
            throw exc;
        }
    }

    private void runOneTest(Method method) throws ReflectiveOperationException {
        Object instance = classOfTest.getDeclaredConstructor().newInstance();
        for (Method beforeMethod : methodsWithAnnotation.get(BeforeEach.class)) {
            beforeMethod.invoke(instance);
        }
        method.invoke(instance);
        for (Method afterMethod : methodsWithAnnotation.get(AfterEach.class)) {
            afterMethod.invoke(instance);
        }
    }
}
