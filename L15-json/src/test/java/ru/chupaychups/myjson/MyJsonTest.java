package ru.chupaychups.myjson;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@DisplayName("Тест проверяющий что моя реализация JSON")
class MyJsonTest {

    private TestJsonClass createTestObject() {
        Random random = new Random();
        int[] intArray = new int[]{random.nextInt(),  random.nextInt(),  random.nextInt()};
        return new TestJsonClass(random.nextLong() + "_TEST",
        random.nextInt(), intArray);
    }

    @Test
    @DisplayName("Корректно преобразует произвольный обьект в строку")
    void myJsonCorrectlyTransformCustomObjectToString() {
        TestJsonClass testJsonClass = createTestObject();
        Assertions.assertThat(new MyJson().toJson(testJsonClass)).isEqualTo(new Gson().toJson(testJsonClass));
    }

    @Test
    @DisplayName("Корректно преобразует коллекцию произвольных обьектов в строку")
    void myJsonCorrectlyTransformCollectionOfCustomObjectsToString() {
        var myObjectList = List.of(createTestObject(), createTestObject(), createTestObject());
        Assertions.assertThat(new MyJson().toJson(myObjectList)).isEqualTo(new Gson().toJson(myObjectList));
    }

    @Test
    @DisplayName("Корректно преобразует масив произвольных обьектов в строку")
    void myJsonCorrectlyTransformArrayOfCustomObjectsToString() {
        var myObjectArray = new TestJsonClass[] {createTestObject(), createTestObject(), createTestObject()};
        Assertions.assertThat(new MyJson().toJson(myObjectArray)).isEqualTo(new Gson().toJson(myObjectArray));
    }

    @DisplayName("Корректно преобразует различные вариации входных параметров, с примитивными типами, в строку")
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
