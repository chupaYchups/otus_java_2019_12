package ru.chupaYchups;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {


    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                IntStream.range(1, 10).forEach(value -> print(value));
                IntStream.range(9, 2).forEach(value -> print(value));
            }
        });
    }


    private static void print(int number) {
        sleep(1000);
        System.out.println(number);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
