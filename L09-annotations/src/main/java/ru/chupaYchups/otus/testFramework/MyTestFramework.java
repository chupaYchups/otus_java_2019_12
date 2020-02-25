package ru.chupaYchups.otus.testFramework;


import ru.chupaYchups.otus.testFramework.test.runner.TestRunner;

public class MyTestFramework {

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("There are too many command line params for program");
        }
        Class testClass;
        String className = args[0];
        try {
            testClass = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Have no such test class : " + className);
        }
        TestRunner runner = new TestRunner(testClass);
        try {
            runner.run();
        } catch (ReflectiveOperationException exc) {
            exc.printStackTrace();
            throw new RuntimeException("Test running failed with exception : " + exc.toString());
        }
    }
}
