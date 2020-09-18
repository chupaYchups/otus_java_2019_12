package ru.chupaYchups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final SimpMessagingTemplate template;

    private static Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    public WebsocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedDelay = 10000)
    public void broadcastCurrentTime() {
        this.template.convertAndSend("/topic/userAdd", "hello!");
    }

    @MessageMapping("/message")
    //@SendTo("/topic/response.{roomId}")
    public void getMessage(String messageString) {
        logger.info("got message:{}", messageString);
    }
    
}
