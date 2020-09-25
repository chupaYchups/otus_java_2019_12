package ru.chupaYchups.dto;

import ru.otus.messagesystem.client.ResultDataType;

public class UserData extends ResultDataType {

    private final long id;
    private final String name;
    private final String login;
    private final String password;

    public UserData(long id, String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.id = id;
    }

    public UserData(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.id = 0;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }
}
