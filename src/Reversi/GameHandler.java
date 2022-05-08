package Reversi;

import java.awt.event.*;

public class GameHandler extends MouseAdapter{
    GameScreen gameScn;
    private boolean PlayerWhite;
    private boolean TurnWhite;
    // 1: 흰색, 0: 돌이 없는 상태, -1: 검은색
    private int[][] board = new int[9][9]; // 8 x 8

    GameHandler(GameScreen gameScn) {
        this.gameScn = gameScn;
        PlayerWhite = true;
        TurnWhite = true;

        for (int i=1; i<=8; i++) // 보드 초기화
            for (int j=1; j<=8; j++)
                board[i][j] = 0;
    }

    public void changeTurn() {
        TurnWhite = !TurnWhite;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (TurnWhite != PlayerWhite) // 내 턴이 아닌 입력 무시
            return;

        int x = getCell(e.getX()), y = getCell(e.getY());
        if ((x <= 0 || x >= 9) || (y <= 0 || y >= 9)) // == (x < 70 || x >= 630) || (y < 70 || y >= 630)
            return;

        board[y][x] = PlayerWhite ? 1 : -1;
        gameScn.repaint();
    }

    public int getCell(int p) {
        return p / 70;
    }

    int getStone(int x, int y) {
        return board[y][x];
    }
}
