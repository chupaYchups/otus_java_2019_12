package ru.chupaychups.myjson;

import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;

class MyJsonTest {

    @Test
    void objectToJsonTest() {
        Long myLong = 4L;
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(myLong)).isEqualTo(gson.toJson(myLong));
    }

    @Test
    void objectCollectionToJsonTest() {
        var myDoubleList = List.of(1.33, 1.25, 19);
        Gson gson = new Gson();
        MyJson myJson = new MyJson();

    }

    @Test
    void primitiveArrayToJsonTest() {
        int[] myIntPrimitiveArray = new int[]{1,2,3};
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(myIntPrimitiveArray)).isEqualTo(gson.toJson(myIntPrimitiveArray));
    }

    @Test
    void primitiveToJsonTest() {
        int myInt = 1;
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(myInt)).isEqualTo(gson.toJson(myInt));
    }


    @Test
    void objectArrayToJsonTest() {
        Integer[] myIntArray = new Integer[]{1,2,3};
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        System.out.println((gson.toJson(myIntArray)));
        Assertions.assertThat(myJson.toJson(myIntArray)).isEqualTo(gson.toJson(myIntArray));
    }

    @Test
    void myObjectToJsonTest() {
        int[] intArray = new int[]{1,  2,  3};
        TestJsonClass testJsonClass = new TestJsonClass("123123", 3, intArray);
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(testJsonClass)).isEqualTo(gson.toJson(testJsonClass));
    }

    @Test
    void myObjectCollectionToJsonTest() {
        int[] intArray = new int[]{1,  2,  3};
        var myObjectList = List.of(new TestJsonClass("3Text", 3, intArray),
            new TestJsonClass("1Text", 1, intArray),
            new TestJsonClass("2Text", 2, intArray));
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(myObjectList)).isEqualTo(gson.toJson(myObjectList));
    }

    @Test
    void myObjectArrayToJsonTest() {
        int[] intArray = new int[]{1,  2,  3};
        var myObjectArray = new TestJsonClass[] {new TestJsonClass("3Text", 3, intArray),
                new TestJsonClass("1Text", 1, intArray),
                new TestJsonClass("2Text", 2, intArray)};
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(myObjectArray)).isEqualTo(gson.toJson(myObjectArray));
    }

    @Test
    void nullTest(){
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(null)).isEqualTo(gson.toJson(null));
    }

    @ParameterizedTest
    @MethodSource("generateDataForCustomTest")
    void customTest(Object o){
        Gson gson = new Gson();
        MyJson myJson = new MyJson();
        Assertions.assertThat(myJson.toJson(o)).isEqualTo(gson.toJson(o));
    }

    private static Stream<Arguments> generateDataForCustomTest() {
        return Stream.of(
                null,
                Arguments.of(true), Arguments.of(false),
                Arguments.of((byte)1), Arguments.of((short)2f),
                Arguments.of(3), Arguments.of(4L), Arguments.of(5f), Arguments.of(6d),
                Arguments.of("aaa"), Arguments.of('b'),
                Arguments.of(new byte[] {1, 2, 3}),
                Arguments.of(new short[] {4, 5, 6}),
                Arguments.of(new int[] {7, 8, 9}),
                Arguments.of(new float[] {10f, 11f, 12f}),
                Arguments.of(new double[] {13d, 14d, 15d}),
                Arguments.of(List.of(16, 17, 18)),
                Arguments.of(Collections.singletonList(19))
        );
    }
}
