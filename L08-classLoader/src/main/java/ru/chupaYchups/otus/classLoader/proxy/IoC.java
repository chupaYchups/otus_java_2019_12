package ru.chupaYchups.otus.classLoader.proxy;

import ru.chupaYchups.otus.classLoader.ITestLogging;
import ru.chupaYchups.otus.classLoader.MainClass;
import ru.chupaYchups.otus.classLoader.TestLogging;
import ru.chupaYchups.otus.classLoader.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IoC {

    public static ITestLogging createLoggingProxy() {
       MyInvocationHandler handler = new MyInvocationHandler(Log.class, new TestLogging());
       return (ITestLogging) Proxy.newProxyInstance(MainClass.class.getClassLoader(), new Class<?>[]{ITestLogging.class}, handler);
    }

    private static class MyInvocationHandler implements InvocationHandler {

        private ITestLogging testLoggingImpl;

        private List<Method> methodsToLog = new ArrayList<>();

        public MyInvocationHandler(Class<? extends Annotation> annotationClass, ITestLogging testLoggingImpl) {
            this.testLoggingImpl = testLoggingImpl;
            collectLoggingMethods(annotationClass, testLoggingImpl);
        }

        private void collectLoggingMethods(Class<? extends Annotation> annotationClass, ITestLogging testLoggingImpl) {
            Class implementationClass = testLoggingImpl.getClass();
            Arrays.stream(implementationClass.getMethods()).
                filter(method -> method.isAnnotationPresent(annotationClass)).
                forEach(method -> {
                    Arrays.stream(implementationClass.getInterfaces()).forEach(interfaceClass -> {
                        Method interfaceMethod = null;
                        try {
                            interfaceMethod = interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                        } catch (NoSuchMethodException e) {
                        }
                        if (interfaceMethod != null) {
                            methodsToLog.add(interfaceMethod);
                        }
                    });
                });
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
            if (methodsToLog.contains(method)) {
                System.out.println("executed method: " + method.getName() + ", param: " + objects[0]);
            }
            return method.invoke(testLoggingImpl, objects);
        }
    }
}
