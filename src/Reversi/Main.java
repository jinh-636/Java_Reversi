package Reversi;

import javax.swing.*;
import java.awt.*;

public class Main{
    public static void main(String[] args) {
        GameScreen gmscreen = new GameScreen();
        Point p = gmscreen.getLocation();
        ChatScreen chscreen = new ChatScreen(p.x+gmscreen.getWidth(), p.y);
    }
}

class GameScreen extends JFrame {
    Startscreen startscreen = new Startscreen();
    GameScreen() {
        setTitle("Hello!");
        setSize(720, 720); // 720 x 720
        setResizable(false); // 창 크기 변경 x
        setLocationRelativeTo(null); // 가운데에서 화면 출력
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼을 통해 프로그램 종료. 사용자에게 물어볼 수 있음.

        add(startscreen);
        setVisible(true);
    }
}

class ChatScreen extends JFrame {
    ChatScreen(int x, int y) {
        setTitle("Chat");
        setSize(480, 480); // 480 x 480
        setResizable(false); // 창 크기 변경 x
        setLocation(x, y); // 게임창 옆에 챗창을 위치 시킴.

        setVisible(true); // 시작 시에는 보이지 않아야 함. 현재는 확인용으로 true.
    }
}