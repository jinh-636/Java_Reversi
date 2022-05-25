package Reversi;

import java.io.*;
import java.net.Socket;

public class DataHandler {
    Socket socket;
    Sender sender;
    Receiver receiver;

    DataHandler() {
        Thread senderThread = new Sender();
        Thread receiverThread = new Receiver();
    }
    class Sender extends Thread {
        PrintWriter pw = null;
        @Override
        public synchronized void start() {
            try {
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            } catch (IOException e) {
                System.out.println("통신이 원할하지 않습니다.");
            }
        }

        @Override
        public void run() {
            super.run();
        }

        public boolean send(String data, char flag) {
            if (flag == 'g') {
                pw.println("game" + data);
                return true;
            }
            else if (flag == 'c') {
                pw.println("chat" + data);
                return true;
            }
            else if (flag == 't') {
                pw.println("trig" + data);
                return true;
            }
            else if (flag == 'b') {
                pw.println("bool" + data);
                return true;
            }

            return false;
        }
    }

    class Receiver extends Thread {
        BufferedReader bw = null;

        @Override
        public synchronized void start() {
            try {
                bw = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.out.println("통신이 원활하지 않습니다.");
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String data = bw.readLine();
                    if (data == null) break;

                    String flag = data.substring(0, 4);
                    data = data.substring(4);
                    if (flag.equals("game")) {

                    }
                    else if (flag.equals("chat")) {

                    }
                    else if (flag.equals("trig")) {

                    }
                    else if (flag.equals("bool")) {

                    }
                } catch (IOException e) {
                    System.out.println("송신에 실패했습니다.");
                }
            }
        }
    }

    public Sender getSender() {
        return this.sender;
    }

    public Receiver getReceiver() {
        return this.receiver;
    }
}
