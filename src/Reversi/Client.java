package Reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    Socket socket = null;
    DataHandler dataHdr;

    public static void main(String[] args) {
        Client client = new Client();
    }

    Client() {
        MainScreen mainScn = new MainScreen();
        Point p = mainScn.getLocation();
        SubScreen subScn = new SubScreen(p.x+mainScn.getWidth(), p.y);
        mainScn.getSubScreen(subScn);
    }

    public void startConnect() {
        // 서버 생성
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1"); // localhost
            socket = new Socket(address, 8888);
            dataHdr = new DataHandler(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class MainScreen extends JFrame implements ActionListener {
        StartScreen startScn;
        SubScreen subScn;
        GameScreen gameScn;
        GameHandler gameHdr;

        MainScreen() {
            setTitle("Hello! - Client");
            setSize(720, 720); // 720 x 720
            setResizable(false); // 창 크기 변경 x
            setLocationRelativeTo(null); // 가운데에서 화면 출력
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼을 통해 프로그램 종료. 사용자에게 물어볼 수 있음.

            startScn = new StartScreen();
            startScn.st_btn.addActionListener(this);

            add(startScn);

            setVisible(true);
        }

        public void getSubScreen(SubScreen subScn) {
            this.subScn = subScn; // 메인화면과 서브화면 연결
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (btn == startScn.st_btn) {
                startConnect();

                remove(startScn);
                setTitle("Othello - Client");
                gameScn = new GameScreen(); // 게임 화면 생성
                gameHdr = new GameHandler(gameScn, dataHdr, false); // 화면과 핸들러 연결
                gameScn.getHandler(gameHdr);
                subScn.chatScn.getHandler(gameHdr);
                dataHdr.getHandler(gameHdr);

                add(gameScn);
                revalidate();
                repaint();
            }
        }
    }

    class SubScreen extends JFrame {
        chatScreen chatScn;
        SubScreen(int x, int y) {
            setTitle("Chat");
            setSize(480, 480); // 480 x 480
            setResizable(false); // 창 크기 변경 x
            setLocation(x, y); // 게임창 옆에 챗창을 위치 시킴.
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            chatScn = new chatScreen();

            setVisible(true); // 시작 시에는 보이지 않아야 함. 현재는 확인용으로 true.
            add(chatScn);
        }
    }
}
