package Reversi;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    Timer timer;
    Thread threadTimer;
    JLabel Turn;
    GameHandler gameHdr;

    GameScreen(boolean isServer) {
        setLayout(null); // 직접 위치를 조작하기 위해

        timer = new Timer(); // 타이머 초기화
        threadTimer = new Thread(timer);
        threadTimer.start();
        add(timer);

        Turn = new JLabel(isServer ? "My Turn!" : "");
        Turn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        Turn.setForeground(new Color(0x29141A));
        Turn.setBounds(15, 640, 120, 30);
        add(Turn);
    }

    public void getHandler(GameHandler gameHdr) {
        this.gameHdr = gameHdr;
        addMouseListener(gameHdr); // 이벤트 리스너로 핸들러 추가
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBoard(g);
        drawStone(g);
    }

    public void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i=1; i<=9; i++) {
            int k = i*70;
            g.drawLine(70, k, 630, k);
            g.drawLine(k, 70, k,630);
        }
    }

    public void drawStone(Graphics g) {
        for (int i=1; i<=8; i++)
            for (int j=1; j<=8; j++) {
                int stone = gameHdr.getStone(j, i);
                switch (stone) {
                    case 1:
                        drawWhite(g, j, i);
                        break;
                    case -1:
                        drawBlack(g, j, i);
                        break;
                    default:
                        break;
                }
            }
    }

    public void drawBlack(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.fillOval(x*70, y*70, 70, 70);
    }

    public void drawWhite(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillOval(x*70, y*70, 70, 70);
    }

    // Timer 내부 클래스
    class Timer extends JLabel implements Runnable {
        private final int MAX_TIME = 15;
        private int second = MAX_TIME;

        Timer() {
            setFont(new Font("맑은 고딕", Font.BOLD, 15));
            setForeground(new Color(0x29141A));
            setBounds(15, 15, 80, 30);
            setText("Time: " + second);
        }

        @Override
        public void run() {
            while (true) {
                while (second >= 0) {
                    try {
                        setText("Time: " + second);
                        second--;
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                try {
                    gameHdr.selectRandomCell();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        public void resetTimer() {
            second = MAX_TIME;
        }

        public void stopTimer() {
            threadTimer.interrupt();
        }

        public void setNoTimer() {
            setText("Time: -");
        }
    }
}

