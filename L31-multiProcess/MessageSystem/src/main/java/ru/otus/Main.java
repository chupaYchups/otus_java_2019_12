package ru.otus;

import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.socket.SocketServer;
import ru.otus.messagesystem.socket.SocketServerImpl;

public class Main {

    public static void main(String[] args) {

        MessageSystem messageSystem = new MessageSystemImpl();
        messageSystem.start();

        SocketServer socketServerService = new SocketServerImpl(messageSystem);
        socketServerService.startServer();
    }
}
