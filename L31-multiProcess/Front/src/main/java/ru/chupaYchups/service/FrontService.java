package ru.chupaYchups.service;

public interface FrontService {
    void createUser(String userName, String login, String password);
    void getUserList();
}
