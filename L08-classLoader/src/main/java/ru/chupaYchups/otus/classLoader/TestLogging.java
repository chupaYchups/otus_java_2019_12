package ru.chupaYchups.otus.classLoader;

import ru.chupaYchups.otus.classLoader.annotation.Log;

public class TestLogging implements ITestLogging {

    @Log
    @Override
    public void calculation(int param) {
    }

    @Override
    public void calculationNotLogging(int param) {
    }
}
