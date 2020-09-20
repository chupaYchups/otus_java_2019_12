package ru.chupaYchups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.chupaYchups.front.FrontServiceImpl;

@Controller
public class WebsocketController {

    private final FrontServiceImpl frontService;

    private static Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    public WebsocketController(FrontServiceImpl frontService) {
        this.frontService = frontService;
    }

    @MessageMapping("/message")
    public void createUser(UserInfoMessage message) {
        logger.info("got message:{}", message);
        frontService.createUser(message.getName(), message.getLogin(), message.getPassword());
    }

    @MessageMapping("/messageTwo")
    public void getUserList() {
        frontService.getUserList();
    }
    
}
