package Reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main{
    public static void main(String[] args) {
        MainScreen mainScn = new MainScreen();
        Point p = mainScn.getLocation();
        SubScreen subScn = new SubScreen(p.x+mainScn.getWidth(), p.y);
    }
}

class MainScreen extends JFrame implements ActionListener {
    StartScreen startScn = new StartScreen();
    GameScreen gameScn = new GameScreen();
    MainScreen() {
        setTitle("Hello!");
        setSize(720, 720); // 720 x 720
        setResizable(false); // 창 크기 변경 x
        setLocationRelativeTo(null); // 가운데에서 화면 출력
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼을 통해 프로그램 종료. 사용자에게 물어볼 수 있음.

        startScn.st_btn.addActionListener(this);
        add(startScn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (btn == startScn.st_btn) {
            remove(startScn);
            setTitle("Othello");
            add(gameScn);
            revalidate();
            repaint();
        }
    }
}

class SubScreen extends JFrame {
    SubScreen(int x, int y) {
        setTitle("Chat");
        setSize(480, 480); // 480 x 480
        setResizable(false); // 창 크기 변경 x
        setLocation(x, y); // 게임창 옆에 챗창을 위치 시킴.

        setVisible(true); // 시작 시에는 보이지 않아야 함. 현재는 확인용으로 true.
    }
}