package ru.chupaYchups.otus.gc;

import java.util.Arrays;

public class MemoryTerror {

    public static final int PAUSE_TO_HELP_COLLECTOR = 10000;
    private long addingElementsCounter;
    private String[] stringArray = new String[10];

    public void run() throws InterruptedException {
        while (true) {
            int oldSize = stringArray.length;
            stringArray = Arrays.copyOf(stringArray, 2 * oldSize);
            for (int i = 0; i < stringArray.length; i++) {
                stringArray[i] = Long.toString(++addingElementsCounter);
            }
            Thread.sleep(PAUSE_TO_HELP_COLLECTOR);
        }
    }

    public long getAddingElementsCounter() {
        return addingElementsCounter;
    }
}
