package ru.chupaychups.myjson;

import ru.chupaychups.visitor.ToJsonStringVisitor;

public class MyJson {

    public String toJson(Object obj) {
        ToJsonStringVisitor visitor = new ToJsonStringVisitor();
        ToJsonStringVisitor.ProcessOperation<String> resultOperation = visitor.inspectObject(obj);
        return resultOperation.execute();
    }
}
