package ru.chupaychups.myjson;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.chupaychups.visitor.ToJsonStringVisitor;

import java.util.List;

public class MyJsonTest2 {

    @Test
    void test1() {

        TestClass testClass = new TestClass();
        testClass.setField1("1");
        testClass.setField2(new String[] {"1", "2", "3"});
        testClass.setField3(List.of(4, 5, 6));
        testClass.setField4('f');

        Gson gson = new Gson();
        System.out.println(gson.toJson(testClass));

        ToJsonStringVisitor visitor = new ToJsonStringVisitor();
        System.out.println(visitor.inspectObject(testClass).execute());

        int[] intArray = new int[]{1,2,3};
        System.out.println(gson.toJson(intArray));
        System.out.println(visitor.inspectObject(intArray).execute());

        Integer myInt = 6;
        System.out.println(gson.toJson(myInt));
        System.out.println(visitor.inspectObject(myInt).execute());

        Integer myIntNull = null;
        System.out.println(gson.toJson(myIntNull));
        System.out.println(visitor.inspectObject(myIntNull).execute());


        TestClass myObj = new TestClass();
        System.out.println(gson.toJson(myObj));
        System.out.println(visitor.inspectObject(myObj).execute());
    }

    private class TestClass {

        private String field1;
        private String[] field2;
        private List<Integer> field3;
        private Character field4;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String[] getField2() {
            return field2;
        }

        public void setField2(String[] field2) {
            this.field2 = field2;
        }

        public List<Integer> getField3() {
            return field3;
        }

        public void setField3(List<Integer> field3) {
            this.field3 = field3;
        }

        public Character getField4() {
            return field4;
        }

        public void setField4(Character field4) {
            this.field4 = field4;
        }
    }
}
