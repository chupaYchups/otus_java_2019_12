package ru.chupaYchups.otus.gc;

import java.util.ArrayList;
import java.util.List;

public class MemoryTerror {

    private List<Integer> intList = new ArrayList<>();

    public void start() throws InterruptedException {
        while (true) {
            int size = intList.size();
            if (size > 1) {
                int elementToDelCount = Math.round(size / 2);
                List subList = intList.subList(0, elementToDelCount - 1);
                subList.clear();
                for (int i = 0; i < 2 * elementToDelCount; i++) {
                    intList.add(7);
                }
            } else {
                intList.add(7);
            }
            Thread.sleep(5000);
        }
    }
}
