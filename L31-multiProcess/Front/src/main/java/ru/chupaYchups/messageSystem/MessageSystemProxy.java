package ru.chupaYchups.messageSystem;

import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;

public class MessageSystemProxy implements MessageSystem {

    @Override
    public void addClient(MsClient msClient) {

    }

    @Override
    public void removeClient(String clientId) {

    }

    @Override
    public boolean newMessage(Message msg) {
        return false;
    }

    @Override
    public void dispose() throws InterruptedException {

    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {

    }

    @Override
    public void start() {

    }

    @Override
    public int currentQueueSize() {
        return 0;
    }
}
