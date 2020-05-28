package ru.chupaychups.myjson;

import ru.chupaychups.visitor.ToJsonStringVisitor;

public class MyJson {

    public String toJson(Object obj) {
        return new ToJsonStringVisitor().inspectObject(obj).get();
    }
}
