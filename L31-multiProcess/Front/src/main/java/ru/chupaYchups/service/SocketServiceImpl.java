package ru.chupaYchups.service;

import org.springframework.stereotype.Service;
import ru.chupaYchups.config.MsSystemConnectionProps;

import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class SocketServiceImpl implements SocketService {

    private final String host;
    private final int port;

    private final BlockingQueue<String> messageQueue;

    public SocketServiceImpl(MsSystemConnectionProps props) {
        this.host = props.getHost();
        this.port = props.getPort();
        messageQueue = new ArrayBlockingQueue<>(10);
    }

    public void start() {
        try(Socket socket = new Socket(host, port);
            PrintStream printStream = new PrintStream(socket.getOutputStream(), true)) {
            while (!Thread.currentThread().isInterrupted()) {
                String message = messageQueue.take();
                printStream.println(message);
            }
        } catch (Exception e) {
            //возможно здесь как-то сделать перезапуск подключения... В случае какой-то ошибки ( c паузой)
        } finally {
            start();
        }
    }

//    @Override
//    public Optional<PrintStream> getSocketOutput() {
//        PrintStream printStream;
//        try(Socket socket = getSocket()) {
//
//            printStream = new PrintStream(socket.getOutputStream(), true);
//
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Error while create connection to socket server");
//            throw new RuntimeException(e);
//        }
//        return Optional.ofNullable(printStream);
//    }
//
//    private Socket getSocket() throws IOException {
//        if (currentSocket == null ||
//                currentSocket.isClosed() ||
//                !currentSocket.isConnected() ||
//                currentSocket.isOutputShutdown()) {
//            currentSocket = new Socket(host, port);
//        }
//        return currentSocket;
//    }


    @Override
    public void sendMessageToSocket(String message) {
       messageQueue.add(message);
    }
}
