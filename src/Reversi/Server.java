package Reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
    ServerSocket serverSocket = null;
    Socket socket = null;
    MainScreen mainScn;
    SubScreen subScn;
    OverScreen overScn;
    DataHandler dataHdr;
    public static void main(String[] args) {
        Server server = new Server();
    }

    Server() {
        mainScn = new MainScreen();
        Point p = mainScn.getLocation();
        subScn = new SubScreen(p.x+mainScn.getWidth(), p.y);
        overScn = new OverScreen();
    }

    public void startConnect() {
        // 서버 생성
        try {
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
            dataHdr = new DataHandler(socket);
        } catch (IOException ex) {
            System.out.println("서버 생성에 실패했습니다.");
            System.exit(1);
        }
    }

    class MainScreen extends JFrame implements ActionListener {
        StartScreen startScn;
        GameScreen gameScn;
        GameHandler gameHdr;

        MainScreen() {
            setTitle("Hello! - Server");
            setSize(720, 720); // 720 x 720
            setResizable(false); // 창 크기 변경 x
            setLocationRelativeTo(null); // 가운데에서 화면 출력
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼을 통해 프로그램 종료. 사용자에게 물어볼 수 있음.

            startScn = new StartScreen();
            startScn.st_btn.addActionListener(this);
            add(startScn);
            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (btn == startScn.st_btn) {
                startConnect();

                remove(startScn);
                setTitle("Othello - Server");
                gameScn = new GameScreen(true); // 게임 화면 생성
                gameHdr = new GameHandler(gameScn, overScn, dataHdr, true); // 화면과 핸들러 연결
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
            chatScn = new chatScreen();
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

            setVisible(true); // 시작 시에는 보이지 않아야 함. 현재는 확인용으로 true.
            add(chatScn);
        }
    }
}