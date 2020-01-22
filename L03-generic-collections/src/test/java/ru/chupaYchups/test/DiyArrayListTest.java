package ru.chupaYchups.test;

import org.junit.Assert;
import org.junit.Test;
import ru.chupaYchups.otus.DiyArrayList;

import java.util.*;
import java.util.stream.Stream;

/**
 * Тест работоспособности реализации списка DiyArrayList
 */
public class DiyArrayListTest {

    public static final int TEST_DATA_DEFAULT_SIZE = 200;

    @Test
    public void testAddAll() {
        List<String> okResultList = new ArrayList<>(TEST_DATA_DEFAULT_SIZE);
        List<String> additionalElements = Arrays.asList("testString1", "testString2", "testString3", "testString4", "testString5");
        List<String> myTestList = new DiyArrayList<>();
        for (int i = 0; i < TEST_DATA_DEFAULT_SIZE; i++) {
            String testStr = "test " + i;
            myTestList.add(testStr);
            okResultList.add(testStr);
        }
        okResultList.addAll(additionalElements);

        //Вызов тестируемого метода
        Collections.addAll(myTestList, "testString1", "testString2", "testString3", "testString4", "testString5");

        Assert.assertEquals(myTestList.size(), TEST_DATA_DEFAULT_SIZE + 5);
        Assert.assertTrue(Arrays.equals(myTestList.toArray(), okResultList.toArray()));
    }

    @Test
    public void testCopy() {
        List<String> myTestListSrc = new DiyArrayList<>();
        String[] copiedArray = new String[TEST_DATA_DEFAULT_SIZE];
        for (int i = 0; i < TEST_DATA_DEFAULT_SIZE; i++) {
            String text = "test " + "i";
            myTestListSrc.add(text);
            copiedArray[i] = text;
        }
        List<String> myTestListDest = new DiyArrayList<>(TEST_DATA_DEFAULT_SIZE);

        //Вызов тестируемого метода
        Collections.copy(myTestListDest, myTestListSrc);

        Assert.assertEquals(myTestListDest.size(),TEST_DATA_DEFAULT_SIZE);
        Assert.assertTrue(Arrays.equals(myTestListDest.toArray(), copiedArray));
    }

    @Test
    public void testSort() {
        final String[] orderedStrings = {"testString2", "testString3", "testString4", "testString5", "testString6"};
        List<String> myTestList = new DiyArrayList<>();
        Collections.addAll(myTestList, "testString4", "testString5", "testString3", "testString2", "testString6");

        //Вызов тестируемого метода
        Collections.sort(myTestList, Comparator.naturalOrder());

        Assert.assertTrue(Arrays.equals(myTestList.toArray(), orderedStrings));
    }


}
