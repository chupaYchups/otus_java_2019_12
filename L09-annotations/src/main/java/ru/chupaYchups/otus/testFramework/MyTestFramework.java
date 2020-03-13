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
        TestRunner.RunResult result;
        result = runner.run();
        logTestResults(result);
    }

    private static void logTestResults(TestRunner.RunResult result) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("==================================Statistics====================================").append(System.lineSeparator());
        stringBuilder.append("Test runned : " + result.getRunTestCounter()).append(System.lineSeparator());
        stringBuilder.append("Successfully runned : " + result.getSuccessful()).append(System.lineSeparator());
        stringBuilder.append("Failed tests : " + result.getFailed());
        System.out.println(stringBuilder.toString());
        result.getErrorInfo().forEach((methodName, exc) -> {
            System.out.println("Failed method - " + methodName + ", stackTrace : ");
            exc.printStackTrace();
        });
    }
}
