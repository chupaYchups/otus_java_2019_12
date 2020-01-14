package ru.otus.l011;

import com.google.common.primitives.Floats;

public class HelloOtus {
    public static void main(String[] args) {
        float f1 = 1.1f;
        float f2 = 2.2f;
        System.out.println(Floats.compare(f1, f2));
    }
}
