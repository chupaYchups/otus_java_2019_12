package ru.chupaYchups.otus.classLoader;

public interface ITestLogging {
    void calculation(int param);
    void calculation(String param);
    void calculationNotLogging(int param);
    void calculation(String param1, int param2);
}
