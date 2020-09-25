package ru.otus.messagesystem.message;

import ru.otus.messagesystem.client.ResultDataType;

public class RegisterMessage extends ResultDataType {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "name='" + name + '\'' +
                '}';
    }
}
