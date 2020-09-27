package ru.chupaYchups;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.chupaYchups.service.SocketService;

@SpringBootApplication
public class FrontMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(FrontMain.class, args);
        SocketService socketService = (SocketService) ctx.getBean("socketServiceImpl");
        socketService.start();
    }


}
