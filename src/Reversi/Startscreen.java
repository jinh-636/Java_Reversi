package Reversi;

import javax.swing.*;
import java.awt.*;

// 시작 시 실행될 화면, 가능하면 대기 화면까지 포함하여 구현될 수 있음
public class Startscreen extends JPanel {
    Startscreen() {
        setLayout(null); // 직접 위치를 조작하기 위해

        JButton st_btn = new JButton("시 작");
        st_btn.setBounds(280, 480, 180, 30);
        add(st_btn);
    }
}
