package ru.chupaYchups.web.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
