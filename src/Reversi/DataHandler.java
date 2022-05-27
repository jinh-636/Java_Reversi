package Reversi;

import java.io.*;
import java.net.Socket;

public class DataHandler {
    Socket socket;
    Sender sender;
    Thread receiverThread;
    GameHandler gameHdr;
    chatScreen chatScn;

    DataHandler(Socket socket) {
        this.socket=socket;
        sender=new Sender();
        receiverThread=new Receiver();
        receiverThread.start();
    }

    public void getHandler(GameHandler gameHdr,chatScreen chatScn){
        this.gameHdr=gameHdr;
        this.chatScn=chatScn;
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
        public void send(String data,char flag){
            if(flag=='g'){
                pw.println("game"+data);
                pw.flush();
            }
            else if(flag=='c'){
                pw.println("chat"+data);
                pw.flush();
            }
            else if(flag=='t'){
                pw.println("trig"+data);
                pw.flush();
            }
            else if(flag=='b'){
                pw.println("bool"+data);
                pw.flush();
            }
        }
        public void closeConnection(){
            pw.println("goodbye");
            pw.flush();
        }
    }
    class Receiver extends Thread {
        BufferedReader bw = null;

        Receiver(){
            try{
                bw=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }catch(IOException e){
                System.out.println("통신이 원할하지 않습니다");
            }
        }
        @Override
        public void run() {
            while (true) {
                try {
                    String data = bw.readLine();
                    if (data.equals("goodbye")){
                        System.out.println("통신이 종료되었습니다.");
                        break;
                    }

                    String flag = data.substring(0, 4);
                    data = data.substring(4);
                    if (flag.equals("game")) {
                        gameHdr.receiveCellData(data.charAt(0)-'0',data.charAt(1)-'0');
                    }
                    else if (flag.equals("chat")) {
                        chatScn.receiveMessage(data);
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
