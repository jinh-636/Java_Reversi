package Reversi;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class DataHandler {
    Socket socket;
    Sender sender;
    Thread receiverThread;
    GameHandler gameHdr;

    DataHandler(Socket socket) {
        this.socket = socket;
        sender = new Sender();
        receiverThread = new Receiver();
        receiverThread.start();
    }

    public void getHandler(GameHandler gameHdr) {
        this.gameHdr = gameHdr;
    }

    class Sender {
        PrintWriter pw = null;
        Sender() {
            try {
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            } catch (IOException e) {
                System.out.println("통신이 원할하지 않습니다.");
            }
        }

        public boolean send(String data, char flag) {
            if (flag == 'g') {
                pw.println("game" + data);
                pw.flush();
                return true;
            }
            else if (flag == 'c') {
                pw.println("chat" + data);
                pw.flush();
                return true;
            }
            else if (flag == 't') {
                pw.println("trig" + data);
                pw.flush();
                return true;
            }
            else if (flag == 'b') {
                pw.println("bool" + data);
                pw.flush();
                return true;
            }

            return false;
        }

        public void closeConnection() {
            pw.println("goodbye");
        }
    }

    class Receiver extends Thread {
        BufferedReader bw = null;

        Receiver() {
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
                    if (data.equals("goodbye")) {
                        // 양쪽이 모두 종료될 수 있도록
                        sender.closeConnection();
                        break;
                    }

                    String flag = data.substring(0, 4);
                    data = data.substring(4);
                    if (flag.equals("game")) {
                        gameHdr.receiveCellData(data.charAt(0) - '0', data.charAt(1) - '0');
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
}
