package ru.chupaYchups.test;

import org.junit.Assert;
import org.junit.Test;
import ru.chupaYchups.otus.DiyArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Тест работоспособности реализации списка DiyArrayList
 */
public class DiyArrayListTest {

    @Test
    public void testAddAll() {
        List<String> myTestList = new DiyArrayList<>();
        Collections.addAll(myTestList, "testString1", "testString2", "testString3", "testString4", "testString5");
        Assert.assertEquals(myTestList.size(), 5);
    }

    @Test
    public void testCopy() {
        List<String> myTestListSrc = new DiyArrayList<>();
        for (int i = 0; i < 2000; i++) {
            myTestListSrc.add("src" + i);
        }
        List<String> myTestListDest = new DiyArrayList<>(2000);
        Collections.copy(myTestListDest, myTestListSrc);
        Assert.assertEquals(myTestListDest.size(),2000);
    }

    @Test
    public void testSort() {
        List<String> myTestList = new DiyArrayList<>();
        Collections.sort(myTestList, Comparator.naturalOrder());
    }


}
