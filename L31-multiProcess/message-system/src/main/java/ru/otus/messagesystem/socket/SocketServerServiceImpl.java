package ru.otus.messagesystem.socket;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.MsClientSocket;
import ru.otus.messagesystem.message.RegisterMessage;
import java.io.BufferedReader;
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
    private final Gson gson;

    @Autowired
    public SocketServerServiceImpl(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        gson = new Gson();
    }

    @Override
    public void startServer() {
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while(!Thread.currentThread().isInterrupted()) {
                try(Socket socket = serverSocket.accept()){
                   handleClientSocket(socket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket(Socket socket) {
        try (PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
             //String input = null;
             String input = in.readLine();
             System.out.println("input from socket : " + input);
             addClientToMsSystem(socket, input);
             outputStream.println("Hello!");

            input = in.readLine();
            System.out.println("input from socket : " + input);
            //addClientToMsSystem(socket, input);
            outputStream.println("Hello!");

            while (!"stop".equals(input)) {
                input = in.readLine();
                if (input != null) {
                    System.out.println("input from socket : " + input);
                    outputStream.println("Hello!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClientToMsSystem(Socket socket, String input) {
        RegisterMessage registerMessage = gson.fromJson(input, RegisterMessage.class);
        MsClientSocket msClientSocket = new MsClientSocket(socket, registerMessage.getName());
        messageSystem.addClient(msClientSocket);
        System.out.println("Client registered!!!");
    }
}
