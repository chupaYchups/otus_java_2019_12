package ru.chupaYchups.otus.testFramework.test.runner;

import ru.chupaYchups.otus.testFramework.annotations.AfterEach;
import ru.chupaYchups.otus.testFramework.annotations.BeforeEach;
import ru.chupaYchups.otus.testFramework.annotations.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class TestRunner {

    private final Class classOfTest;
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

    public RunResult run() {
        RunResult result = new RunResult();
        for (Method testMethod : methodsWithAnnotation.get(Test.class)) {
            try {
                result.incrementRunCounter();
                runOneTest(testMethod);
                result.incrementSuccessCounter();
            } catch (Exception e) {
                result.incrementFailedCounter();
                result.putErrorInfo(testMethod.getName(), e);
            }
        }
        return result;
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

    public class RunResult {

        private int runTestCounter;
        private int successful;
        private int failed;
        private Map<String, Exception> failedTests = new HashMap<>();

        public int getRunTestCounter() {
            return runTestCounter;
        }
        public int getSuccessful() {
            return successful;
        }
        public int getFailed() {
            return failed;
        }
        public void incrementRunCounter() {
            this.runTestCounter++;
        }
        public void incrementSuccessCounter() {
            this.successful++;
        }
        public void incrementFailedCounter() {
            this.failed++;
        }
        public void putErrorInfo(String methodName, Exception exception) {
            failedTests.put(methodName, exception);
        }
        public Map<String, Exception> getErrorInfo() {
            return failedTests;
        }
    }
}
