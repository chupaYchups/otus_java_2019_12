package ru.otus.messagesystem.socket;

import com.google.gson.Gson;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.MsClientProxy;
import ru.otus.messagesystem.message.Message;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SocketServerImpl implements SocketServer {

    private final MessageSystem messageSystem;
    private final static int SERVER_PORT = 8090;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final Gson gson;
    private final List<String> registeredClientNames;

    public SocketServerImpl(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.gson = new Gson();
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        registeredClientNames = new ArrayList<>();
    }

    @Override
    public void startServer() {
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected : " + socket.toString());
                    threadPoolExecutor.execute(() -> handleClientSocket(socket));
                }  catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    private void registerClient(Socket socket, PrintWriter outputStream,  BufferedReader in) throws IOException {
        String input = in.readLine();
        System.out.println("input from socket : " + input);
        addClientToMsSystem(socket, input);
        outputStream.println("client add : OK");
    }

    private void addClientToMsSystem(Socket socket, String input) {
        RegisterMessage registerMessage = gson.fromJson(input, RegisterMessage.class);
        MsClientSocket msClientSocket = new MsClientSocket(socket, registerMessage.getName());
        messageSystem.addClient(msClientSocket);
        System.out.println("Client registered : " + registerMessage.getName());
    }*/

    private void handleClientSocket(Socket socket) {
        try (socket; PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
 //           registerClient(socket, outputStream, in);
            String input = null;
            while (!"stop".equals(input)) {
                input = in.readLine();
                if (input != null) {
                    System.out.println("Input from socket : " + input);
//                    processMessage(input, socket);
                    outputStream.println("----->> OK");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void processMessage(String input, Socket socket) {
        Message message = gson.fromJson(input, Message.class);
        if (!registeredClientNames.contains(message.getFrom())) {
            messageSystem.addClient(new MsClientProxy(socket, (message.getFrom())));
        }
        messageSystem.newMessage(message);
    }
}
