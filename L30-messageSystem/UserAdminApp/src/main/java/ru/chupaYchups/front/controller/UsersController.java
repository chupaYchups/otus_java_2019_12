package ru.chupaYchups.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    @GetMapping({"/"})
    public String userListView() {
        return "userList.html";
    }
}
