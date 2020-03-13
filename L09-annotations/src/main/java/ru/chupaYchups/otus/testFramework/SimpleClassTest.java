package ru.chupaYchups.otus.testFramework;

import ru.chupaYchups.otus.testFramework.annotations.AfterEach;
import ru.chupaYchups.otus.testFramework.annotations.BeforeEach;
import ru.chupaYchups.otus.testFramework.annotations.Test;

public class SimpleClassTest {

    @BeforeEach
    public void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("after each");
    }

    @Test
    public void testAdd() {
        System.out.println("----SimpleClassTest.testAdd");
        throw new RuntimeException();
    }

    @Test
    public void testMutiply() {
        System.out.println("----SimpleClassTest.testMutiply");
    }

    @Test
    public void testDivide() {
        System.out.println("----SimpleClassTest.testDivide");
    }
}
