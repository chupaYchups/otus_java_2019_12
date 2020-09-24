package ru.otus.messagesystem.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.messagesystem.MessageSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SocketServerServiceImpl implements SocketServerService {

    private final MessageSystem messageSystem;

    private final static int SERVER_PORT = 8090;

    private final ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    public SocketServerServiceImpl(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    @Override
    public void startServer() {
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while(!Thread.currentThread().isInterrupted()) {
                try(Socket socket = serverSocket.accept()){
                    threadPoolExecutor.submit(() -> handleClientSocket(socket));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket(Socket socket) {
        try (/*PrintWriter outputStreqm = new PrintWriter(socket.getOutputStream(), true);*/
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String input = null;
            while (!"stop".equals(input)) {
                input = in.readLine();
                if (input != null) {
                    System.out.println("input from socket : " + input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
