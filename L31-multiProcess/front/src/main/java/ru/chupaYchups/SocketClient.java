package ru.chupaYchups;

import com.google.gson.Gson;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.RegisterMessage;
import java.io.BufferedReader;
import java.io.IOException;
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

                registerToServer(outputStream, in);

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

    private void registerToServer(PrintWriter outputStream, BufferedReader in) throws IOException {
        Gson gson = new Gson();
        RegisterMessage regMsg = new RegisterMessage();
        regMsg.setName("front");
        outputStream.println(gson.toJson(regMsg));
        System.out.println(String.format("sending to server: %s", regMsg));
        System.out.println(String.format("server response: %s", in.readLine()));
    }
}