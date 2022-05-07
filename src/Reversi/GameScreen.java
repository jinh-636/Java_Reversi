package Reversi;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class GameScreen extends JPanel {
    // int[][] board = new int[9][9]; // 8 x 8
    Timer timer;
    Thread threadTimer;

    GameScreen() {
        setLayout(null); // 직접 위치를 조작하기 위해

        timer = new Timer();
        threadTimer = new Thread(timer);
        threadTimer.start();
        add(timer);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for (int i=1; i<=9; i++) {
            int k = i*70;
            g.drawLine(70, k, 630, k);
            g.drawLine(k, 70, k,630);
        }
    }
}

class Timer extends JLabel implements Runnable {
    private int second = 15;

    Timer() {
        setFont(new Font("맑은 고딕", Font.BOLD, 15));
        setForeground(new Color(0x29141A));
        setBounds(15, 15, 80, 30);
        setText("Time: " + second);
    }

    @Override
    public void run() {
        while (second > 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            second--;
            setText("Time: " + second);
        }
    }
}
