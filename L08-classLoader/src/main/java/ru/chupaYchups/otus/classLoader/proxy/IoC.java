package ru.chupaYchups.otus.classLoader.proxy;

import ru.chupaYchups.otus.classLoader.ITestLogging;
import ru.chupaYchups.otus.classLoader.MainClass;
import ru.chupaYchups.otus.classLoader.TestLogging;
import ru.chupaYchups.otus.classLoader.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class IoC {

    public static ITestLogging createLoggingProxy() {
       MyInvocationHandler handler = new MyInvocationHandler(Log.class, new TestLogging());
       return (ITestLogging) Proxy.newProxyInstance(MainClass.class.getClassLoader(), new Class<?>[]{ITestLogging.class}, handler);
    }

    private static class MyInvocationHandler implements InvocationHandler {

        private final ITestLogging testLoggingImpl;

        private final List<Method> methodsToLog;

        public MyInvocationHandler(Class<? extends Annotation> annotationClass, ITestLogging testLoggingImpl) {
            this.testLoggingImpl = testLoggingImpl;
            methodsToLog = collectLoggingMethods(annotationClass, testLoggingImpl);
        }

        private List<Method> collectLoggingMethods(Class<? extends Annotation> annotationClass, ITestLogging testLoggingImpl) {
            Class implementationClass = testLoggingImpl.getClass();
            UnaryOperator<Method> findMethodInInterfaces = method -> {
                for (Class interfaceClass : implementationClass.getInterfaces()) {
                    try {
                        return interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException e) {
                    }
                }
                return null;
            };
            return Arrays.stream(implementationClass.getMethods()).
                    filter(method -> method.isAnnotationPresent(annotationClass)).
                    map(findMethodInInterfaces).collect(Collectors.toList());
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
