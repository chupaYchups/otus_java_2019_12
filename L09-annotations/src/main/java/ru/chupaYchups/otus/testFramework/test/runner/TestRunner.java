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

    private HashMap<Class<? extends Annotation>, List<Method>> annotationMethods;

    public TestRunner(Class testClass) {
        this.classOfTest = testClass;
        annotationMethods = new HashMap<>();
        supportedAnnotationClasses.forEach(aClass -> annotationMethods.put(aClass, new ArrayList<>()));
    }

    private void collectTestMethods(Class testClass) {
        annotationMethods = new HashMap<>();
        for (Method declaredMethod : testClass.getDeclaredMethods()) {
            for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
                if (supportedAnnotationClasses.contains(declaredAnnotation.getClass())) {
                    annotationMethods.get(declaredAnnotation.getClass()).add(declaredMethod);
                }
            }
        }
    }

    public void run() {
        collectTestMethods(classOfTest);
        List<Method> beforeTestMethods = annotationMethods.get(BeforeEach.class);
        List<Method> afterTestMethods = annotationMethods.get(AfterEach.class);
        try {
            for (Method method : annotationMethods.get(Test.class)) {
                runOneTest(method, beforeTestMethods, afterTestMethods);
            }
        } catch (ReflectiveOperationException exc) {
            System.out.println("Test running failed with exception : " + exc.toString());
            exc.printStackTrace();
        }
    }

    private void runOneTest(Method method, List<Method> beforeMethods, List<Method> afterMethods) throws ReflectiveOperationException {
        Object instance;
        instance = classOfTest.getDeclaredConstructor().newInstance();
        for (Method beforeMethod : beforeMethods) {
            beforeMethod.invoke(instance);
        }
        method.invoke(instance);
        for (Method afterMethod : afterMethods) {
            afterMethod.invoke(instance);
        }
    }
}
