package Reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class chatScreen extends JPanel implements ActionListener{
    JLabel chat_label;
    JTextArea chat_log;//챗기록

    JTextField chat_space;//챗입력공간
    JButton button_input;//입력버튼
    JScrollPane scrollPane;//스크롤

    GameHandler gameHdr; // 핸들러 연결

    //CHAT SCREEN
    chatScreen(){
        setLayout(null);
        Font font=new Font("궁서 보통",Font.BOLD,15);
        Font font1=new Font("HyhwpEQ 보통",Font.PLAIN,15);
        chat_label=new JLabel("chat에 입장하셨습니다. 도움이 필요하시면 /help를 눌러주세요.");
        chat_label.setBounds(0,0,467,20);
        chat_label.setFont(font);


        chat_log=new JTextArea();
        chat_log.setEditable(false);//수정x
        //chat_log.setBounds(0,20,467,390);
        chat_log.setBackground(Color.LIGHT_GRAY);//백그라운드 color 지정
        chat_log.setFont(font1);

        chat_space=new JTextField(10);//최대 10갯수만큼의 텍스트 입력가능
        chat_space.setBounds(0,410,370,31);

        button_input=new JButton("Send");
        button_input.setBackground(Color. YELLOW);//배경색
        button_input.setForeground(Color.BLACK);//폰트색
        button_input.setBounds(370,410,97,31);


        scrollPane=new JScrollPane(chat_log,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0,20,467,390);



        add(chat_label);
        add(chat_space);
        add(button_input);
        add(scrollPane);

        button_input.addActionListener(this);

        chat_space.requestFocus();
    }

    public void getHandler(GameHandler gameHdr) {
        this.gameHdr = gameHdr;
    }

    //Send 눌렀을 때
    @Override
    public void actionPerformed(ActionEvent e) {
        sendMessage();
    }

    public void sendMessage() {
        String help_command="Command: /help,/rule,/restart,/reset,/ff\n"
                +"/help: command에 관한 설명\n"
                +"/rule: 오델로게임 룰 설명\n"
                +"/restart: 게임재시작요청-> 상대방에게 게임재시작의사를 물어봅니다\n"
                +"/reset: 되돌리기요청-> 방금 든 수를 무를 수 있는지 상대방에게 물어봅니다.\n"
                +"/ff: 항복\n";
        String rule_command="처음에는 정 중앙에 흑백 2개의 돌을 교차로 놓고 게임을 시작한다.\n"
                +"돌을 놓을 때, 자신이 놓을 돌과 자신의 돌 사이에 상대편의 돌이 있어야 돌을 놓을 수 있으며, 돌을 놓아서 자기편의 돌 사이에 상대편의 돌이 끼어 있는 형태를 만들게 되면 따먹을 수 있다.\n" +
                " 단, 따먹은 돌은 없어지는 게 아니라 자기편의 돌로 변한 채로 그 자리에 있게 된다. 즉, 상대편의 돌을 뒤집어 자기편의 돌로 만드는 것이다.\n";

        String text = chat_space.getText();

        chat_log.append(text + "\n");//챗기록창에 chat남김

            //입력된 메세지가 /help일 경우
        if(text.equals("/help")) {
            chat_log.append("----\n");
            chat_log.append(help_command);//help_command
            chat_log.append("----\n");
        }
        else if(text.equals("/rule")){
            chat_log.append("----\n");
            chat_log.append(rule_command);//rule_command
            chat_log.append("----\n");
        }
        else if(text.equals("/restart")){

        }
        else if(text.equals("/reset")){

        }
        else if(text.equals("/ff")){
            gameHdr.checkGameOver(true, false);
        }
        else{

        }

        //클라이언트에게 메세지 전송

        //초기화 및 커서요청
        chat_space.setText("");
        chat_space.requestFocus();
    }
}


