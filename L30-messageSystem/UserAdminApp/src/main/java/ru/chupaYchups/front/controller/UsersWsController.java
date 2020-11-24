package ru.chupaYchups.front.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.chupaYchups.front.service.FrontServiceImpl;

@Controller
public class UsersWsController {

    private final FrontServiceImpl frontService;

    private static Logger logger = LoggerFactory.getLogger(UsersWsController.class);

    public UsersWsController(FrontServiceImpl frontService) {
        this.frontService = frontService;
    }

    @MessageMapping("/userCreate")
    public void createUser(UserInfoMessage message) {
        logger.info("got message:{}", message);
        frontService.createUser(message.getName(), message.getLogin(), message.getPassword());
    }

    @MessageMapping("/getUserList")
    public void getUserList() {
        frontService.sendUsersListToClient();
    }
}
