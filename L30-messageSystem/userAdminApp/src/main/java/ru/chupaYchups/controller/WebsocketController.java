package ru.chupaYchups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.chupaYchups.front.FrontServiceImpl;

@Controller
public class WebsocketController {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //private final SimpMessagingTemplate template;
    private final FrontServiceImpl frontService;

    private static Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    public WebsocketController(FrontServiceImpl frontService) {
      //  this.template = template;
        this.frontService = frontService;
    }

    //@Scheduled(fixedDelay = 10000)
    //public void broadcastCurrentTime() {
    //    this.template.convertAndSend("/topic/users", "hello!");
    //}

    @MessageMapping("/message")
    //@SendTo("/topic/response.{roomId}")
    public void getMessage(UserInfoMessage message) {
        logger.info("got message:{}", message);
        frontService.createUser(message.getName(), message.getLogin(), message.getPassword());
    }
    
}
