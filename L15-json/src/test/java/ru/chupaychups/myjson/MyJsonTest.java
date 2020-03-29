package ru.chupaychups.myjson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import java.util.List;

class MyJsonTest {

    @Test
    void objectToJsonTest() {
        Long myLong = 4L;
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertEquals(myJson.toJson(myLong), gson.toJson(myLong));
    }

    @Test
    void objectCollectionToJsonTest() {
        var myDoubleList = List.of(1.33, 1.25, 19);
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertEquals(myJson.toJson(myDoubleList), gson.toJson(myDoubleList));
    }

    @Test
    void primitiveArrayToJsonTest() {
        int[] myIntPrimitiveArray = new int[]{1,2,3};
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertEquals(myJson.toJson(myIntPrimitiveArray), gson.toJson(myIntPrimitiveArray));
    }

    @Test
    void primitiveToJsonTest() {
        int myInt = 1;
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertEquals(myJson.toJson(myInt), gson.toJson(myInt));
    }


    @Test
    void objectArrayToJsonTest() {
        Integer[] myIntArray = new Integer[]{1,2,3};
        Gson gson = new Gson();
        MyJson myJson = new MyJson();

        System.out.println((gson.toJson(myIntArray)));

        Assertions.assertEquals(myJson.toJson(myIntArray), gson.toJson(myIntArray));
    }

    @Test
    void myObjectToJsonTest() {
        int[] intArray = new int[]{1,  2,  3};
        TestJsonClass testJsonClass = new TestJsonClass("123123", 3, intArray);
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertEquals(gson.toJson(testJsonClass), myJson.toJson(testJsonClass));
    }

    @Test
    void myObjectCollectionToJsonTest() {
        int[] intArray = new int[]{1,  2,  3};
        var myObjectList = List.of(new TestJsonClass("3Text", 3, intArray),
            new TestJsonClass("1Text", 1, intArray),
            new TestJsonClass("2Text", 2, intArray));
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertEquals(myJson.toJson(myObjectList), gson.toJson(myObjectList));
    }

    @Test
    void myObjectArrayToJsonTest() {
        int[] intArray = new int[]{1,  2,  3};
        var myObjectArray = new TestJsonClass[] {new TestJsonClass("3Text", 3, intArray),
                new TestJsonClass("1Text", 1, intArray),
                new TestJsonClass("2Text", 2, intArray)};
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertEquals(myJson.toJson(myObjectArray), gson.toJson(myObjectArray));
    }
}
