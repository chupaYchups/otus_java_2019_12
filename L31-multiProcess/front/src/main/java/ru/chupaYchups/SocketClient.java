package ru.chupaYchups;

import com.google.gson.Gson;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messagesystem.message.RegisterMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class SocketClient {
    private static final int PORT = 8090;
    private static final String HOST = "localhost";

    MsClient msClient;

    public static void main(String[] args) {
        new SocketClient().go();
    }

    private void go() {

        msClient = new MsClientImpl("front", new MessageSystemImpl(), new HandlersStoreImpl(),
                new CallbackRegistryImpl());

        try {
            try (Socket clientSocket = new Socket(HOST, PORT)) {

                PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

//                String msg = gson.toJson(registerMessage);

                RegisterMessage regMsg = new RegisterMessage();
                regMsg.setName("front");

                Message message  = msClient.produceMessage("db", regMsg, MessageType.USER_LIST, t -> System.out.println("hello!"));

                Gson gson = new Gson();
                outputStream.println(gson.toJson(regMsg));
                System.out.println(String.format("sending to server: %s", regMsg));
                System.out.println(String.format("server response: %s", in.readLine()));

                outputStream.println(gson.toJson(message));
                System.out.println(String.format("sending to server: %s", message));
                System.out.println(String.format("server response: %s", in.readLine()));

                for (int idx = 0; idx < 3; idx++) {
                    String msg = String.format("##%d - I Believe", idx);
                    System.out.println(String.format("sending to server: %s", msg));
                    outputStream.println(msg);

                    String responseMsg = in.readLine();
                    System.out.println(String.format("server response: %s", responseMsg));
                    Thread.sleep(TimeUnit.SECONDS.toMillis(3));

                    System.out.println();
                }

                System.out.println("\nstop communication");
                outputStream.println("stop");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}