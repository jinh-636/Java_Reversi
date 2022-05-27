package Reversi;

import javax.swing.*;
import java.awt.*;

public class OverScreen extends JFrame {
    JLabel textLabel;

    OverScreen() {
        setTitle("Game Over!");
        setSize(250, 90);
        setResizable(false); // 창 크기 변경 x
        setLocationRelativeTo(null); // 가운데에서 화면 출력
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼을 통해 프로그램 종료. 사용자에게 물어볼 수 있음.

        textLabel = new JLabel("test");
        textLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        textLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel panel = new JPanel();
        panel.add(textLabel);
        add(panel);
        setVisible(false);
    }
}
