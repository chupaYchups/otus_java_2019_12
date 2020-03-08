package ru.chupaYchups.otus.gc;

import java.util.Arrays;

public class MemoryTerror {

    private long addingElementsCounter;
    private String[] stringArray = new String[10];

    public void run() {
        while (true) {
            int oldSize = stringArray.length;
            stringArray = Arrays.copyOf(stringArray, 3 * oldSize);
            for (int i = 0; i < stringArray.length; i++) {
                String newObj = Long.toString(++addingElementsCounter);
                System.out.println(newObj);
                stringArray[i] = newObj;
            }
        }
    }

    public long getAddingElementsCounter() {
        return addingElementsCounter;
    }
}
