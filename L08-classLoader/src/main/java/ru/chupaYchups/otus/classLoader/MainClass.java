package ru.chupaYchups.otus.classLoader;
import ru.chupaYchups.otus.classLoader.proxy.IoC;

public class MainClass {
    public static void main(String[] args) {
        ITestLogging testLogging = IoC.createLoggingProxy();
        testLogging.calculation(6);
        testLogging.calculationNotLogging(6);
        testLogging.calculation("Hello guys!!!");
        testLogging.calculation("Hello", 2);
    }
}
