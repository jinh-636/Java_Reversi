package Reversi;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
    ServerSocket serverSocket = null;
    Socket socket = null;
    DataHandler dataHdr;

    Server() {
        try {
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
            dataHdr = new DataHandler();

            Thread receiver = dataHdr.getReceiver();
            Thread sender = dataHdr.getSender();
            receiver.start();
            sender.start();
        } catch (IOException e) {
            System.out.println("서버 생성에 실패했습니다.");
        }
        finally {
            try {
                serverSocket.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
