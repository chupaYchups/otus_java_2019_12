package ru.chupaYchups.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;

import java.util.List;

@Controller
public class UsersController {

    private final DBServiceUser dbServiceUser;

    public UsersController(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @GetMapping({"/user/create"})
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return "userCreate.html";
    }

    @GetMapping({"/"})
    public String userListView(Model model) {
        List<User> users = dbServiceUser.findAllUsers().get();
        model.addAttribute("users", users);
        return "userList.html";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute User user) {
        dbServiceUser.saveUser(user);
        return new RedirectView("/", true);
    }

    @GetMapping("/hello1")
    public String helloView(Model model) {
        return "hello.html";
    }
}
