package ru.chupaYchups.otus.gc;

import java.util.ArrayList;
import java.util.List;

public class MemoryTerror {

    public static final int ELEMENT_VALUE = 7;
    private int addingElementsCounter;

    private List<Integer> intList = new ArrayList<>();

    public void start() throws InterruptedException {
        while (true) {
            int size = intList.size();
            if (size > 1) {
                int elementToDelCount = Math.round(size / 2);
                List subList = intList.subList(0, elementToDelCount - 1);
                subList.clear();
                for (int i = 0; i < 2 * elementToDelCount; i++) {
                    addElement();
                }
            } else {
                addElement();
            }
            Thread.sleep(7000);
        }
    }

    private void addElement() {
        intList.add(ELEMENT_VALUE);
        addingElementsCounter++;
    }

    public int getAddingElementsCounter() {
        return addingElementsCounter;
    }
}
