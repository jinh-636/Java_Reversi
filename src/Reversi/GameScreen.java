package Reversi;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    // int[][] board = new int[9][9]; // 8 x 8

    GameScreen() {
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
