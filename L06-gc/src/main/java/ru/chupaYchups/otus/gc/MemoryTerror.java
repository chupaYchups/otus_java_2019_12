package ru.chupaYchups.otus.gc;

import java.util.Arrays;

public class MemoryTerror {

    private final static int DELAY = 3000;
    private long addingElementsCounter;
    private String[] stringArray = new String[10];

    public void start() throws InterruptedException {
        while (true) {
            int oldSize = stringArray.length;
            stringArray = Arrays.copyOf(stringArray, 3 * oldSize);
            for (int i = 0; i < stringArray.length; i++) {
                stringArray[i] = Long.toString(++addingElementsCounter);
               // System.out.println(addingElementsCounter);
            }
            //Thread.sleep(5000);
        }
    }

    public long getAddingElementsCounter() {
        return addingElementsCounter;
    }
}
