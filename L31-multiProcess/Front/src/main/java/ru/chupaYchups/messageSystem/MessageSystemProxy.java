package ru.chupaYchups.messageSystem;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import ru.chupaYchups.service.SocketService;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageSystemProxy implements MessageSystem {

    private final SocketService socketService;

    private final Gson gson;

    private final Map<String, MsClient> clientMap;

    public MessageSystemProxy(SocketService socketService, Gson gson) {
        this.socketService = socketService;
        this.gson = gson;
        clientMap = new HashMap<>();
    }

    @Override
    public void addClient(MsClient msClient) {
        clientMap.put(msClient.getName(), msClient);
    }

    @Override
    public void removeClient(String clientId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean newMessage(Message msg) {
        socketService.sendMessageToSocket(gson.toJson(msg));
        return true;
    }

    @Override
    public void dispose() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int currentQueueSize() {
        throw new UnsupportedOperationException();
    }

    //Надо ли вообще регистрироваться?
//    private void registerClient(MsClient msClient) {
//        RegisterMessage registerMessage = new RegisterMessage();
//        registerMessage.setName(msClient.getName());
//        socketService.getSocketOutput().ifPresentOrElse(printStream -> printStream.println(gson.toJson(registerMessage)), () -> {
//            System.out.println("Cannot get connection to register client");
//            throw new RuntimeException();
//        });
//    }
}
