package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.socket.SocketServerService;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		var context = SpringApplication.run(Main.class, args);

		MessageSystem messageSystem = (MessageSystem)context.getBean("messageSystemImpl");
		messageSystem.start();

		SocketServerService socketServerService = (SocketServerService)context.getBean("socketServerServiceImpl");
		socketServerService.startServer();
	}
}
