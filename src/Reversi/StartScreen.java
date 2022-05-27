package Reversi;

import javax.swing.*;
import java.awt.*;

// 시작 시 실행될 화면, 가능하면 대기 화면까지 포함하여 구현될 수 있음
public class StartScreen extends JPanel {
    JButton st_btn;
    JLabel label1;

    StartScreen() {
        setLayout(null); // 직접 위치를 조작하기 위해
        st_btn = new JButton("시 작");

        st_btn.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        st_btn.setBounds(265, 460, 180, 30);
        ImageIcon icon = new ImageIcon("src/Reversi/image/오델로게임.png"); //Reversi 사진
        label1 = new JLabel(icon);
        label1.setBounds(0,0, 706, 683);

        add(st_btn);
        add(label1);
    }
}
