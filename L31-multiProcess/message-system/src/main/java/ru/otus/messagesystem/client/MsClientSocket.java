package ru.otus.messagesystem.client;

import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

import java.net.Socket;

public class MsClientSocket implements MsClient {

    private final Socket socket;
    private final String name;

    public MsClientSocket(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    @Override
    public boolean sendMessage(Message msg) {
        return false;
    }

    @Override
    public void handle(Message msg) {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T extends ResultDataType> Message produceMessage(String to, T data, MessageType msgType, MessageCallback<T> callback) {
        return null;
    }
}
