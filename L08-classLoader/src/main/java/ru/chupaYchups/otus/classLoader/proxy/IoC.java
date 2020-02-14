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
import java.util.List;

public class IoC {

    public static ITestLogging createLoggingProxy() {
       MyInvocationHandler handler = new MyInvocationHandler(Log.class, new TestLogging());
       return (ITestLogging) Proxy.newProxyInstance(MainClass.class.getClassLoader(), new Class<?>[]{ITestLogging.class}, handler);
    }

    private static class MyInvocationHandler implements InvocationHandler {

        private ITestLogging testLoggingImpl;

        private List<String> logMethodNames = new ArrayList<>();

        public MyInvocationHandler(Class<? extends Annotation> annotationClass, ITestLogging testLoggingImpl) {
            this.testLoggingImpl = testLoggingImpl;
            collectLoggingMethods(annotationClass, testLoggingImpl);
        }

        private void collectLoggingMethods(Class<? extends Annotation> annotationClass, ITestLogging testLoggingImpl) {
            Method[] methods = testLoggingImpl.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotationClass)) {
                    logMethodNames.add(method.getName());
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
            if (logMethodNames.contains(method.getName())) {
                System.out.println("executed method: " + method.getName() + ", param: " + objects[0]);
            }
            return method.invoke(testLoggingImpl, objects);
        }
    }
}
