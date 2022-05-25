package Reversi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    Socket socket = null;
    DataHandler dataHdr;

    Client () {
        try {
            InetAddress address = InetAddress.getByName("localhost");
            socket = new Socket(address, 8888);
            dataHdr = new DataHandler();

            Thread receiver = dataHdr.getReceiver();
            Thread sender = dataHdr.getSender();
            receiver.start();
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
